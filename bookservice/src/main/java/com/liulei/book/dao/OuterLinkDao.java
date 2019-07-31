package com.liulei.book.dao;

import com.liulei.book.po.OuterLink;
import com.liulei.mybatis.paginator.Page;
import com.liulei.mybatis.paginator.PageParam;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description
 * @auther runze
 * @date 2019/7/25 14:36
 */

@Mapper
public interface OuterLinkDao {
    Page<OuterLink> selectPage(PageParam pageParam, OuterLink entry);

}
