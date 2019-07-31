package com.liulei.generator.dao;

import com.liulei.mybatis.paginator.Page;
import com.liulei.mybatis.paginator.PageParam;

import java.util.List;

/**
 * @description: Mapper-继承该接口后，无需编写 mapper.xml 文件，即可获得CRUD功能 这个 Mapper 支持 id 泛型
 * @Author: runze
 * @Date: 2019/7/26 16:25
 */
public interface MybatisBaseDao<T, I> {

    /**
     * @param entity-实体对象
     * @return int
     * @description 插入一条记录
     */
    int insert(T entity);

    /**
     * <@description 插入一条记录（选择字段， null 字段不插入）
     *
     * @param entity-实体对象
     * @return int
     */
    int insertVal(T entity);

    /**
     * @param id-主键ID
     * @return int
     * @description 根据 主键 删除
     */
    int deleteById(I id);

    /**
     * @param idList-主键ID列表
     * @return int
     * @description 删除（根据ID 批量删除）
     */
    int deleteByIds(List<I> idList);

    /**
     * @param entity-实体对象
     * @return int
     * @description 根据 ID 修改
     */
    int updateById(T entity);

    /**
     * @param entity-实体对象
     * @return 影响的记录数
     * @description 根据 ID 选择修改
     */
    int updateValById(T entity);

    /**
     * @param id-主键ID
     * @return T 返回ID对应的对象
     * @description 根据 ID 查询
     */
    T selectById(I id);

    /**
     * @param idList-主键ID列表
     * @return 记录List
     * @description 查询（根据ID 批量查询）
     */
    List<T> selectByIds(List<I> idList);

    /**
     * @param pageParam-分页参数
     * @param entity-实体对象
     * @return 一页记录
     * @description 根据 分页参数和entity对象，查询一页记录
     */
    Page<T> selectPage(PageParam pageParam, T entity);

}
