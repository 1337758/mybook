package com.liulei.book.dao;

import com.liulei.generator.dao.MybatisBaseDao;
import com.liulei.book.po.Article;
import com.liulei.book.vo.ArticleVo;
import com.liulei.mybatis.paginator.Page;
import com.liulei.mybatis.paginator.PageParam;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 *   @description
 *   @auther runze
 *   @date 2019/7/25 14:23
 *
 */

@Mapper
public interface ArticleDao extends MybatisBaseDao<Article, Integer> {
//    Page<Article> selectPage(PageParam descPageParam, Article entry);

    Page<Article> selectPageByVo(PageParam pageParam, ArticleVo entry);

//    Article selectById(Integer id);

//    void insert(Article entity);
}
