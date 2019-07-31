package com.liulei.book.dao;

import com.liulei.generator.dao.MybatisBaseDao;
import com.liulei.book.po.Todo;
import com.liulei.book.vo.TodoVo;
import com.liulei.mybatis.paginator.Page;
import com.liulei.mybatis.paginator.PageParam;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description
 * @auther runze
 * @date 2019/7/25 16:25
 */
@Mapper
public interface TodoDao extends MybatisBaseDao<Todo, Integer> {
    Page<Todo> selectPageByVo(PageParam pageParam, TodoVo entry);

}
