package com.czh.chbackend.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.czh.chbackend.common.PageResult;
import com.czh.chbackend.model.dto.reply.ReplyPageRequest;
import com.czh.chbackend.model.entity.Reply;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author czh
 * @since 2024-05-27
 */
public interface IRepliesService extends IService<Reply> {

    PageResult getPage(ReplyPageRequest request);
}
