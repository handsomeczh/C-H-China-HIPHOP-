package com.czh.chbackend.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author czh
 * @version 1.0.0
 * 2024/5/21 21:18
 */
@Data
@AllArgsConstructor
@ApiModel(value = "Follow对象", description = "关注列表")
public class Follow implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id/粉丝id")
    private Long fanId;

    @ApiModelProperty("用户id/关注id")
    private Long followId;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;


    @ApiModelProperty("粉丝用户名")
    private String fanName;

    @ApiModelProperty("粉丝用户头像")
    private String fanAvatar;


    @ApiModelProperty("名")
    private String followName;

    @ApiModelProperty("关注用户头像")
    private String followAvatar;

    public Follow(User fan, User follow) {
        this.fanId = fan.getId();
        this.followId = follow.getId();

        this.fanAvatar = fan.getUserAvatar();
        this.fanName = fan.getUserName();

        this.followAvatar = follow.getUserAvatar();
        this.followName = follow.getUserName();
    }
}
