package com.czh.chbackend.model.dto.reply;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author czh
 * @version 1.0.0
 * 2024/5/27 13:21
 */
@Data
public class ReplyAddRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long commentId;

    private String content;

}
