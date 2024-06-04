package com.czh.chbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czh.chbackend.common.PageResult;
import com.czh.chbackend.mapper.CommentsMapper;
import com.czh.chbackend.model.dto.comment.CommentPageRequest;
import com.czh.chbackend.model.entity.Comment;
import com.czh.chbackend.service.ICommentsService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author czh
 * @since 2024-05-27
 */
@Service
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comment> implements ICommentsService {

    @Override
    public PageResult getPage(CommentPageRequest request) {
        try {
            IPage<Comment> page = new Page<>(request.getCurrent(), request.getPageSize());
            LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Comment::getSongId,request.getSongId())
                    .orderBy(!request.getIsNew(), true, Comment::getLikeCount)
                    .orderBy(request.getIsNew(), true, Comment::getCreateTime);
            IPage<Comment> commentIPage = this.page(page, wrapper);
            List<Comment> records = commentIPage.getRecords();
            return new PageResult(records.size(), records);
        } catch (Exception e) {
            log.error("Error while fetching comment", e);
            return new PageResult(0, null); // 或者抛出自定义异常
        }
    }
}
