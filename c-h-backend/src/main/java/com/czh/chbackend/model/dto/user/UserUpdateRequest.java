package com.czh.chbackend.model.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author czh
 * @version 1.0.0
 * 2024/5/21 20:33
 */
@Data
public class UserUpdateRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("手机号")
    private String userIphone;

    @ApiModelProperty("密码")
    private String userPassword;

    @ApiModelProperty("用户昵称")
    private String userName;

    @ApiModelProperty("用户头像")
    private String userAvatar;

    @ApiModelProperty("用户简介")
    private String userProfile;

    @ApiModelProperty("性别0.其他1.男2.女")
    private Integer userGender;

    @ApiModelProperty("用户生日")
    private LocalDate userBirthday;

    @ApiModelProperty("用户地区")
    private String userArea;

}
