package com.czh.chbackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author czh
 * @since 2024-05-27
 */
@TableName("comment_likes")
@ApiModel(value = "CommentLikes对象", description = "评论点赞表")
@Data
public class CommentLike implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private Long commentId;

    private Long userId;

    private LocalDateTime createTime;


}
