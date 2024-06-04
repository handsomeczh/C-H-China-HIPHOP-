package com.czh.chbackend.controller.music;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.czh.chbackend.common.PageResult;
import com.czh.chbackend.common.Result;
import com.czh.chbackend.model.dto.comment.CommentAddRequest;
import com.czh.chbackend.model.dto.comment.CommentPageRequest;
import com.czh.chbackend.model.dto.reply.ReplyAddRequest;
import com.czh.chbackend.model.dto.reply.ReplyPageRequest;
import com.czh.chbackend.model.entity.Comment;
import com.czh.chbackend.model.entity.CommentLike;
import com.czh.chbackend.model.entity.Music;
import com.czh.chbackend.model.entity.Reply;
import com.czh.chbackend.service.ICommentLikesService;
import com.czh.chbackend.service.ICommentsService;
import com.czh.chbackend.service.IMusicService;
import com.czh.chbackend.service.IRepliesService;
import com.czh.chbackend.utils.UserContext;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.czh.chbackend.common.CommonConstant.*;
import static com.czh.chbackend.common.ErrorCode.ALREADY_EXIST;
import static com.czh.chbackend.common.ErrorCode.PARAMS_ERROR;

/**
 * 音乐评论前端控制器
 *
 * @author czh
 * @since 2024-05-27
 */
@RestController
@RequestMapping("/music/comments")
public class CommentsController {
    @Autowired
    private ICommentsService commentService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private IMusicService musicService;

    @Autowired
    private ICommentLikesService likesService;

    @Autowired
    private IRepliesService repliesService;

    /**
     * 新增音乐评论
     */
    @PostMapping("/add")
    public Result addComment(@RequestBody CommentAddRequest request) {
        // 判断该歌曲是否存在
        if (!musicService.exists(new LambdaQueryWrapper<Music>().eq(Music::getId, request.getSongId()))) {
            return Result.error(PARAMS_ERROR, "歌曲不存在");
        }
        if (StringUtil.isNullOrEmpty(request.getContent())) {
            return Result.error(PARAMS_ERROR, "音乐评论不能为空");
        }
        Comment comment = new Comment();
        BeanUtils.copyProperties(request, comment);
        comment.setUserId(UserContext.getCurrentId());
        commentService.save(comment);
        return Result.success();
    }

    /**
     * 获取音乐评论：分页查询,默认获取的是热门评论
     * 获取最新评论：通过字段：isNew == true
     */
    @GetMapping("/page")
    public Result<PageResult> getCommentsByMusicId(@RequestBody CommentPageRequest request) {
        // 判断该歌曲是否存在
        if (!musicService.exists(new LambdaQueryWrapper<Music>().eq(Music::getId, request.getSongId()))) {
            return Result.error(PARAMS_ERROR, "歌曲不存在");
        }
        //从Redis获取
        SetOperations<String, String> ops = redisTemplate.opsForSet();
        String key;
        if (request.getIsNew()) {
            key = REDIS_NEW_COMMENTS + request.getSongId();
        } else {
            key = REDIS_HOT_COMMENTS + request.getSongId();
        }
        List<Comment> comments = ops.members(key).stream().map(obj -> JSONUtil.toBean(obj, Comment.class)).collect(Collectors.toList());
        if (comments != null && !comments.isEmpty()) {
            return Result.success(new PageResult(comments.size(), comments));
        }
        // 数据库获取
        PageResult pageResult = commentService.getPage(request);
        // 存入Redis
        for (Object record : pageResult.getRecords()) {
            ops.add(key, JSONUtil.toJsonStr(record));
        }
        redisTemplate.expire(key, 1, TimeUnit.HOURS);
        return Result.success(pageResult);
    }

    /**
     * 删除音乐评论
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteComment(@PathVariable Long id) {
        // 判断该评论是否存在
        Comment comment = commentService.getOne(new LambdaQueryWrapper<Comment>().eq(Comment::getId, id));
        if (comment == null) {
            return Result.error(PARAMS_ERROR, "评论不存在");
        }
        commentService.removeById(id);
        // 删除Redis缓存
        String key = REDIS_HOT_COMMENTS + comment.getSongId();
        redisTemplate.delete(key);
        key = REDIS_NEW_COMMENTS + comment.getSongId();
        redisTemplate.delete(key);
        return Result.success();
    }

    /**
     * 点赞音乐评论
     */
    @PostMapping("/like/{commentId}")
    public Result likeComment(@PathVariable Long commentId) {
        // 判断该评论是否存在
        Comment comment = commentService.getOne(new LambdaQueryWrapper<Comment>().eq(Comment::getId, commentId));
        if (comment == null) {
            return Result.error(PARAMS_ERROR, "评论不存在");
        }
        // 判断是否已经点赞
        Long userId = UserContext.getCurrentId();
        if (likesService.exists(new LambdaQueryWrapper<CommentLike>()
                .eq(CommentLike::getCommentId, commentId)
                .eq(CommentLike::getUserId, userId)
        )) {
            return Result.error(ALREADY_EXIST, "已经点赞");
        }
        // 增加点赞数
        commentService.update(new LambdaUpdateWrapper<Comment>()
                .eq(Comment::getId, commentId)
                .setIncrBy(Comment::getLikeCount, 1)
        );
        // 创建用户点赞关联数据
        CommentLike like = new CommentLike();
        like.setCommentId(commentId);
        like.setUserId(userId);

        likesService.save(like);
        // 清除热门评论缓存
        String key = REDIS_HOT_COMMENTS + comment.getSongId();
        redisTemplate.delete(key);
        return Result.success();
    }

    /**
     * 取消点赞
     */
    @DeleteMapping("/unlike/{commentId}")
    public Result unlikeComment(@PathVariable Long commentId) {
        // 判断该评论是否存在
        Comment comment = commentService.getOne(new LambdaQueryWrapper<Comment>().eq(Comment::getId, commentId));
        if (comment == null) {
            return Result.error(PARAMS_ERROR, "评论不存在");
        }
        // 判断是否点赞
        Long userId = UserContext.getCurrentId();
        if (!likesService.exists(new LambdaQueryWrapper<CommentLike>()
                .eq(CommentLike::getCommentId, commentId)
                .eq(CommentLike::getUserId, userId)
        )) {
            return Result.error(ALREADY_EXIST, "未点赞");
        }
        likesService.remove(new LambdaQueryWrapper<CommentLike>().eq(CommentLike::getCommentId, commentId)
                .eq(CommentLike::getUserId, userId));
        // 点赞数-1；
        commentService.update(new LambdaUpdateWrapper<Comment>()
                .eq(Comment::getId, commentId)
                .setDecrBy(Comment::getLikeCount, 1)
        );
        // 清除热门评论缓存
        String key = REDIS_HOT_COMMENTS + comment.getSongId();
        redisTemplate.delete(key);
        return Result.success();
    }

    /**
     * 音乐评论回复
     */
    @PostMapping("/reply/add")
    public Result addReply(@RequestBody ReplyAddRequest request) {
        // 判断该评论是否存在
        if (!commentService.exists(new LambdaQueryWrapper<Comment>().eq(Comment::getId, request.getCommentId()))) {
            return Result.error(PARAMS_ERROR, "评论不存在");
        }
        if (StringUtil.isNullOrEmpty(request.getContent())) {
            return Result.error(PARAMS_ERROR, "回复不能为空");
        }
        Reply reply = new Reply();
        BeanUtils.copyProperties(request, reply);
        reply.setUserId(UserContext.getCurrentId());
        repliesService.save(reply);
        return Result.success();
    }

    /**
     * 获取音乐评论回复
     */
    @GetMapping("/reply/")
    public Result<PageResult> getRepliesByCommentId(@RequestBody ReplyPageRequest request) {
        // 判断该评论是否存在
        if (!commentService.exists(new LambdaQueryWrapper<Comment>().eq(Comment::getId, request.getCommentId()))) {
            return Result.error(PARAMS_ERROR, "评论不存在");
        }
        //从Redis获取
        SetOperations<String, String> ops = redisTemplate.opsForSet();
        String key= REDIS_REPLY_KEY + request.getCommentId();

        List<Reply> replies = ops.members(key).stream().map(obj -> JSONUtil.toBean(obj, Reply.class)).collect(Collectors.toList());
        if (replies != null && !replies.isEmpty()) {
            return Result.success(new PageResult(replies.size(), replies));
        }
        // 数据库获取
        PageResult pageResult = repliesService.getPage(request);
        // 存入Redis
        for (Object record : pageResult.getRecords()) {
            ops.add(key, JSONUtil.toJsonStr(record));
        }
        redisTemplate.expire(key, 1, TimeUnit.HOURS);
        return Result.success(pageResult);
    }

}
