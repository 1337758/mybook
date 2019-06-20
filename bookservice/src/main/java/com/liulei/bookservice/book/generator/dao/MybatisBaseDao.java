package com.liulei.bookservice.book.generator.dao;

public interface MybatisBaseDao<T, I> {

    int insert(T user);
}
