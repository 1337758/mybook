package com.liulei.book.dao;

import com.liulei.book.po.Const;
import com.liulei.book.vo.ConstVo;
import com.liulei.mybatis.paginator.Page;
import com.liulei.mybatis.paginator.PageParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @description
 * @auther runze
 * @date 2019/7/25 16:04
 */

@Mapper
public interface ConstDao {
    Page<Const> selectPageByVo(PageParam pageParam, ConstVo entry);


    void updateById(Const entity);

    void insert(Const entity);

    List<Const> findAllByDepict(String depict);

    void deleteById(Integer id);
}
