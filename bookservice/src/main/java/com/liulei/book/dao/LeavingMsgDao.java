package com.liulei.book.dao;

import com.liulei.book.po.LeavingMsg;
import com.liulei.book.vo.LeavingMsgVo;
import com.liulei.mybatis.paginator.Page;
import com.liulei.mybatis.paginator.PageParam;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 *   @description
 *   @auther runze
 *   @date 2019/7/25 14:31
 *
 */
@Mapper
public interface LeavingMsgDao {
    Page<LeavingMsgVo> selectPageForVo(PageParam pageParam, LeavingMsg entry);

    void insert(LeavingMsg entity);
}
