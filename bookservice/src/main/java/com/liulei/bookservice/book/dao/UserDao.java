package com.liulei.bookservice.book.dao;

import com.liulei.bookservice.book.generator.dao.MybatisBaseDao;
import com.liulei.bookservice.book.vo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserDao extends MybatisBaseDao<User, Integer> {

    User findUserByUserNameAndPassword(@Param("userCode") String userCode, @Param("password") String password);

    User findUserByUserCode(String userCode);
}
