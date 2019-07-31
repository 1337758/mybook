package com.liulei.book.dao;

import com.liulei.book.po.OperateLog;
import com.liulei.generator.dao.MybatisBaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 表operate_log对应Dao接口<br/>
 */
@Mapper
public interface OperateLogDao extends MybatisBaseDao<OperateLog, Integer> {

}
