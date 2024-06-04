package com.czh.chbackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author czh
 * @since 2024-05-27
 */
@TableName("replies")
@ApiModel(value = "Replies对象", description = "音乐评论回复表")
@Data
public class Reply implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private Long commentId;

    private Long userId;

    private String content;

    private LocalDateTime createTime;


}
