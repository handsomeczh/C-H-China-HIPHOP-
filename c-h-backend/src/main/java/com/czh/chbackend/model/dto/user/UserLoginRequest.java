package com.czh.chbackend.model.dto.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求
 * @author czh
 * @version 1.0.0
 * 2024/5/22 21:24
 */
@Data
@ApiModel(value = "User登录对象", description = "登录表")
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("手机号")
    private String userIphone;

    @ApiModelProperty("密码")
    private String userPassword;

    @ApiModelProperty("验证码")
    private String code;
}
