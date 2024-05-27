package com.czh.chbackend.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.czh.chbackend.common.PageResult;
import com.czh.chbackend.model.dto.comment.CommentPageRequest;
import com.czh.chbackend.model.entity.Comment;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author czh
 * @since 2024-05-27
 */
public interface ICommentsService extends IService<Comment> {


    PageResult getPage(CommentPageRequest request);

}
