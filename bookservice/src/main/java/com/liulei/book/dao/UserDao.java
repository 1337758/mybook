package com.liulei.book.dao;

import com.liulei.book.po.User;
import com.liulei.generator.dao.MybatisBaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserDao extends MybatisBaseDao<User, Integer> {

    User findUserByUserNameAndPassword(@Param("userCode") String userCode, @Param("password") String password);

    User findUserByUserCode(String userCode);

}
