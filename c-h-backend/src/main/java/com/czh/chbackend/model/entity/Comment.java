package com.czh.chbackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author czh
 * @since 2024-05-27
 */
@TableName("comments")
@ApiModel(value = "Comments对象", description = "音乐评论表")
@Data
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private Long songId;

    private Long userId;

    private String content;

    private Integer likeCount;

    private LocalDateTime createTime;


}
