package com.czh.chbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czh.chbackend.mapper.CommentLikesMapper;
import com.czh.chbackend.model.entity.CommentLike;
import com.czh.chbackend.service.ICommentLikesService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author czh
 * @since 2024-05-27
 */
@Service
public class CommentLikesServiceImpl extends ServiceImpl<CommentLikesMapper, CommentLike> implements ICommentLikesService {

}
