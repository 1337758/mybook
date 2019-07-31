package com.liulei.book.service;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.liulei.book.dao.ArticleDao;
import com.liulei.book.po.Article;
import com.liulei.book.vo.ArticleVo;
import com.liulei.book.vo.PageQueryVo;
import com.liulei.common.util.ConstantUtils;
import com.liulei.common.util.ParamUtils;
import com.liulei.mybatis.paginator.Page;
import com.liulei.mybatis.paginator.PageParam;
import com.liulei.mybatis.util.Pages;
import com.liulei.mybatis.util.ResultPage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @description
 * @auther runze
 * @date 2019/7/25 15:53
 */
@Service(value = "articleService")
@Transactional
public class ArticleService {

    @Autowired
    private ArticleDao articleDao;

    public ResultPage<ArticleVo> getPageData(PageQueryVo pageQueryVo) {
        int pageNo = pageQueryVo.getPageNo();
        int size = pageQueryVo.getPageSize();
        PageParam pageParam = ParamUtils.getDescPageParam(pageNo, size > 0 ? size : 10, "update_time");
        ArticleVo entry = new ArticleVo();
        // 搜索条件
        if (StringUtils.isNotBlank(pageQueryVo.getQueryValue())) {
            // 关键字的模糊匹配
            entry.setQueryValue(pageQueryVo.getQueryValue());
        }
        // 类型的匹配
        if (StringUtils.isNotBlank(pageQueryVo.getType())) {
            entry.setType(pageQueryVo.getType());
        }
        if (StringUtils.isNotBlank(pageQueryVo.getClassify())) {
            entry.setClassify(pageQueryVo.getClassify());
        }
        Page<Article> page = articleDao.selectPageByVo(pageParam, entry);
        // 分页数据
        return Pages.convert(pageParam, page, ArticleVo.class);
    }

    public void addArticle(ArticleVo articleVo) {
        Article entity = new Article();
        BeanUtils.copyProperties(entity, articleVo);
        //路径去掉根路径
        entity.setArtImgPath(entity.getArtImgPath().replace(ConstantUtils.FILE_PATH, ""));
        // 作者
        entity.setAuthor("runze");
        // 有效状态
        entity.setStatus(ConstantUtils.STATUS_VALID);
        entity.setLikes(0);
        entity.setViews(0);
        entity.setCommons(0);
        // 插入时间
        entity.setInsertTime(new Date());
        articleDao.insert(entity);
    }
}
