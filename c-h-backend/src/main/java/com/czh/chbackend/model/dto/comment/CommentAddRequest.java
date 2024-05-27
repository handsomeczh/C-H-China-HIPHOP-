package com.czh.chbackend.model.dto.comment;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author czh
 * @version 1.0.0
 * 2024/5/27 11:12
 */
@Data
public class CommentAddRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long songId;

    private String content;

}
