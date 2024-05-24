package com.czh.chbackend.model.dto.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author czh
 * @version 1.0.0
 * 2024/5/22 11:19
 */
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("手机号")
    private String userIphone;

    @ApiModelProperty("验证码")
    private String code;
}
