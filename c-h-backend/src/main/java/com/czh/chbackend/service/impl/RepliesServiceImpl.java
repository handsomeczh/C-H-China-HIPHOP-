package com.czh.chbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czh.chbackend.common.PageResult;
import com.czh.chbackend.mapper.RepliesMapper;
import com.czh.chbackend.model.dto.reply.ReplyPageRequest;
import com.czh.chbackend.model.entity.Comment;
import com.czh.chbackend.model.entity.Reply;
import com.czh.chbackend.service.IRepliesService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务实现类
 *
 * @author czh
 * @since 2024-05-27
 */
@Service
public class RepliesServiceImpl extends ServiceImpl<RepliesMapper, Reply> implements IRepliesService {

    @Override
    public PageResult getPage(ReplyPageRequest request) {
        try {
            IPage<Reply> page = new Page<>(request.getCurrent(), request.getPageSize());
            LambdaQueryWrapper<Reply> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Reply::getCommentId, request.getCommentId())
                    .orderBy(true,true, Reply::getCreateTime);
            IPage<Reply> commentIPage = this.page(page, wrapper);
            List<Reply> records = commentIPage.getRecords();
            return new PageResult(records.size(), records);
        } catch (Exception e) {
            log.error("Error while fetching Reply", e);
            return new PageResult(0, null); // 或者抛出自定义异常
        }
    }
}
