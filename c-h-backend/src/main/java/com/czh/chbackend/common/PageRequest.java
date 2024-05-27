package com.czh.chbackend.common;

import lombok.Data;

import static com.czh.chbackend.common.CommonConstant.SORT_ORDER_ASC;

/**
 */
@Data
public class PageRequest {
    /**
     * 当前页号
     */
    private int current = 1;

    /**
     * 页面大小
     */
    private int pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField = "createTime";

    /**
     * 排序顺序（默认升序）
     */
    private String sortOrder = SORT_ORDER_ASC;
}
