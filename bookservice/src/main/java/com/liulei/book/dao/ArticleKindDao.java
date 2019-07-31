package com.liulei.book.dao;

import com.liulei.book.po.ArticleKind;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @description
 * @auther runze
 * @date 2019/7/25 15:10
 */

@Mapper
public interface ArticleKindDao {
    List<ArticleKind> findAll(String module);

}
