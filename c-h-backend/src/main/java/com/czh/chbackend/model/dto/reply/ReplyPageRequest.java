package com.czh.chbackend.model.dto.reply;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.czh.chbackend.common.PageRequest;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author czh
 * @version 1.0.0
 * 2024/5/27 13:25
 */
@Data
public class ReplyPageRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long commentId;
}
