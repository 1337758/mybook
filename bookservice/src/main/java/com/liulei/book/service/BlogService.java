package com.liulei.book.service;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.liulei.book.dao.ArticleDao;
import com.liulei.book.dao.ArticleKindDao;
import com.liulei.book.dao.LeavingMsgDao;
import com.liulei.book.dao.OuterLinkDao;
import com.liulei.book.po.Article;
import com.liulei.book.po.ArticleKind;
import com.liulei.book.po.LeavingMsg;
import com.liulei.book.po.OuterLink;
import com.liulei.book.vo.*;
import com.liulei.common.util.ConstantUtils;
import com.liulei.common.util.ParamUtils;
import com.liulei.common.vo.TokenMsg;
import com.liulei.mybatis.paginator.Page;
import com.liulei.mybatis.paginator.PageParam;
import com.liulei.mybatis.util.Pages;
import com.liulei.mybatis.util.ResultPage;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @auther runze
 * @date 2019/7/25 11:02
 */
@Service(value = "blogService")
@Transactional
public class BlogService {


    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private LeavingMsgDao leavingMsgDao;

    @Autowired
    private OuterLinkDao outerLinkDao;

    @Autowired
    private ArticleKindDao articleKindDao;

    /**
     * @description: 获取博客首页数据
     * @Author: runze
     * @Date: 2019/7/25 15:06
     */
    public HomeDataVo getHomeData() {
        HomeDataVo homeData = new HomeDataVo();
        //获取读书笔记
        List<ArticleVo> studys = this.findArticlesByModule(3, ConstantUtils.ARTICLE_MODULE_STUDY);
        homeData.setStudys(studys);
        //获取心情随笔
        List<ArticleVo> essays = this.findArticlesByModule(2, ConstantUtils.ARTICLE_MODULE_ESSAY);
        homeData.setEssays(essays);
        // 获取最新留言
        List<LeavingMsgVo> msgs = this.findMsgs(PageParam.NO_PAGE, 10);
        homeData.setMsgs(msgs);
        // 获取外连接
        List<OuterLinkVo> shipLinks = this.findShipLinks(8, ConstantUtils.LINK_TYPE_SHIP);
        homeData.setShipLinks(shipLinks);
        return homeData;
    }

    /**
     * @description: 根据类型查询连接，根据order排序
     * @Author: runze
     * @Date: 2019/7/25 14:36
     */
    private List<OuterLinkVo> findShipLinks(int size, String type) {
        PageParam pageParam = ParamUtils.getAscPageParam(size, "sequence");
        OuterLink entry = new OuterLink();
        entry.setType(type);
        entry.setStatus(ConstantUtils.STATUS_VALID);
        Page<OuterLink> shipLinksPage = outerLinkDao.selectPage(pageParam, entry);
        ResultPage<OuterLinkVo> shipLinksResult = Pages.convert(pageParam, shipLinksPage, OuterLinkVo.class);
        return shipLinksResult.getData();
    }

    /**
     * @description: 获取最新的留言，根据update_time排序
     * @Author: runze
     * @Date: 2019/7/25 14:33
     */
    private List<LeavingMsgVo> findMsgs(int page, int size) {
        PageParam pageParam = ParamUtils.getDescPageParam(page, size, "update_time");
        LeavingMsg entry = new LeavingMsg();
        entry.setStatus(ConstantUtils.STATUS_VALID);
        entry.setType(ConstantUtils.MSG_TYPE_BOARD);
        Page<LeavingMsgVo> msgsVoPage = leavingMsgDao.selectPageForVo(pageParam, entry);
        ResultPage<LeavingMsgVo> msgsResult = Pages.convert(pageParam, msgsVoPage, LeavingMsgVo.class);
        return msgsResult.getData();
    }

    /**
     * @description: 获取不同模块最新的文章，根据update_time排序
     * @Author: runze
     * @Date: 2019/7/25 11:32
     */
    private List<ArticleVo> findArticlesByModule(int size, String module) {
        return this.findArticles(PageParam.NO_PAGE, size, module, "update_time");
    }

    /**
     * @description: 获取不同模块最新的文章，自定义降序
     * @Author: runze
     * @Date: 2019/7/25 11:35
     */
    private List<ArticleVo> findArticles(int page, int size, String module, String order) {
        PageParam descPageParam = ParamUtils.getDescPageParam(page, size, order);
        Article entry = new Article();
        entry.setModule(module);
        entry.setStatus(ConstantUtils.STATUS_VALID);
        Page<Article> studysPage = articleDao.selectPage(descPageParam, entry);
        ResultPage<ArticleVo> convert = Pages.convert(descPageParam, studysPage, ArticleVo.class);
        return convert.getData();
    }


    /**
     * @description: 获取学习笔记页面数据
     * @Author: runze
     * @Date: 2019/7/25 15:07
     */
    public StudyDataVo getStudyData() throws Exception {
        StudyDataVo studyDataVo = new StudyDataVo();
        // 获得读书笔记
        ResultPage<ArticleVo> studyPage = this.findArticlePageByModule(1, 6, ConstantUtils.ARTICLE_MODULE_STUDY);
        studyDataVo.setStudyPage(studyPage);
        // 获取热门文章
        List<ArticleVo> hots = this.findArticles(PageParam.NO_PAGE, 10, ConstantUtils.ARTICLE_MODULE_STUDY, "likes");
        studyDataVo.setHots(hots);
        // 获取文章分类
        List<ArticleKindVo> kinds = this.findArticleKinds(ConstantUtils.ARTICLE_MODULE_STUDY);
        studyDataVo.setKinds(kinds);
        return studyDataVo;
    }

    /**
     * @description: 获取不同模块最新的文章，根据update_time排序
     * @Author: runze
     * @Date: 2019/7/25 15:08
     */
    private ResultPage<ArticleVo> findArticlePageByModule(int page, int size, String module) throws Exception {
        PageParam pageParam = ParamUtils.getDescPageParam(page, size, "update_time");
        Article entry = new Article();
        entry.setModule(module);
        // 有效
        entry.setStatus(ConstantUtils.STATUS_VALID);
        Page<Article> studysPage = articleDao.selectPage(pageParam, entry);
        // 分页数据
        return Pages.convert(pageParam, studysPage, ArticleVo.class);
    }

    /**
     * @description: 取文章分类数据
     * @Author: runze
     * @Date: 2019/7/25 15:09
     */
    private List<ArticleKindVo> findArticleKinds(String module) throws Exception {
        // 重组新的层级分类
        List<ArticleKindVo> kinds = new ArrayList<>();
        List<ArticleKind> ArticleKindsList = articleKindDao.findAll(module);
        Map<String, ArticleKindVo> map = new HashedMap<>(0);
        // 按层级组织
        for (ArticleKind articleKinds : ArticleKindsList) {
            ArticleKindVo articleKindsVo = new ArticleKindVo();
            BeanUtils.copyProperties(articleKindsVo, articleKinds);
            ArticleKindVo outer = null;
            List<ArticleKindVo> inners = null;
            // 文章的type大类型
            String type = articleKinds.getType();
            if (map.containsKey(type)) {
                outer = map.get(type);
                inners = outer.getChildren();
            } else {
                outer = new ArticleKindVo();
                BeanUtils.copyProperties(outer, articleKinds);
                inners = new ArrayList<>();
                // 新创建的对象放入集合中
                kinds.add(outer);
                map.put(type, outer);
            }
            inners.add(articleKindsVo);
            outer.setChildren(inners);
        }
        // 分页数据
        return kinds;
    }

    /**
     * @description: 获取心情随笔页面数据
     * @Author: runze
     * @Date: 2019/7/25 15:13
     */
    public EssayDataVo getEssayData() throws Exception {
        EssayDataVo essayDataVo = new EssayDataVo();
        // 获得读书笔记
        ResultPage<ArticleVo> essayPage = this.findArticlePageByModule(1, 5, ConstantUtils.ARTICLE_MODULE_ESSAY);
        essayDataVo.setEssayPage(essayPage);
        // 获取热门文章
        List<ArticleVo> hots = this.findArticles(PageParam.NO_PAGE, 6, ConstantUtils.ARTICLE_MODULE_ESSAY, "views");
        essayDataVo.setHots(hots);
        // 获取文章分类
        List<ArticleKindVo> kinds = this.findArticleKinds(ConstantUtils.ARTICLE_MODULE_ESSAY);
        essayDataVo.setKinds(kinds);
        return essayDataVo;
    }

    /**
     * @description: 查询文章分页数据 默认更新时间排序
     * @Author: runze
     * @Date: 2019/7/25 15:17
     */
    public ResultPage<ArticleVo> getPageData(PageQueryVo pageQueryVo) {
        int page = pageQueryVo.getPageNo();
        int size = 6;
        if (ConstantUtils.ARTICLE_MODULE_ESSAY.equals(pageQueryVo.getModule())) {
            size = 5;
        }
        PageParam pageParam = ParamUtils.getDescPageParam(page, size, "update_time");
        ArticleVo entry = new ArticleVo();
        entry.setModule(pageQueryVo.getModule());
        // 有效
        entry.setStatus(ConstantUtils.STATUS_VALID);
        // 搜索条件
        if (StringUtils.isNotBlank(pageQueryVo.getQueryValue())) {
            // 关键字的模糊匹配
            entry.setQueryValue(pageQueryVo.getQueryValue());
        }
        // 类型的匹配
        if (StringUtils.isNotBlank(pageQueryVo.getClassify())) {
            entry.setType(pageQueryVo.getType());
            entry.setClassify(pageQueryVo.getClassify());
        }
        Page<Article> studysPage = articleDao.selectPageByVo(pageParam, entry);
        // 分页数据
        return Pages.convert(pageParam, studysPage, ArticleVo.class);
    }

    /**
     * @description: 获取留言板分页信息
     * @Author: runze
     * @Date: 2019/7/25 15:20
     */
    public BoardDataVo getBoardData() {
        BoardDataVo boardDataVo = new BoardDataVo();
        ResultPage<LeavingMsgVo> msgPage = findMsgPage(1, 6);
        // 查询
        boardDataVo.setMsgPage(msgPage);
        return boardDataVo;
    }

    /**
     * @description: 获取留言信息
     * @Author: runze
     * @Date: 2019/7/25 15:23
     */
    public ResultPage<LeavingMsgVo> findMsgPage(int page, int size) {
        PageParam pageParam = ParamUtils.getDescPageParam(page, size, "update_time");
        LeavingMsg entry = new LeavingMsg();
        entry.setStatus(ConstantUtils.STATUS_VALID);
        // 留言类型
        entry.setType(ConstantUtils.MSG_TYPE_BOARD);
        Page<LeavingMsgVo> msgsVoPage = leavingMsgDao.selectPageForVo(pageParam, entry);
        return Pages.convert(pageParam, msgsVoPage, LeavingMsgVo.class);
    }

    /**
     * @description: 获取文章留言信息
     * @Author: runze
     * @Date: 2019/7/25 15:24
     */
    public ResultPage<LeavingMsgVo> findArticleMsgPage(Integer id, int page, int size) {
        PageParam pageParam = ParamUtils.getDescPageParam(page, size, "update_time");
        LeavingMsg entry = new LeavingMsg();
        entry.setStatus(ConstantUtils.STATUS_VALID);
        // 留言类型
        entry.setType(ConstantUtils.MSG_TYPE_ARTICLE);
        // 文章id
        entry.setArticleId(id);
        Page<LeavingMsgVo> msgsVoPage = leavingMsgDao.selectPageForVo(pageParam, entry);
        return Pages.convert(pageParam, msgsVoPage, LeavingMsgVo.class);
    }

    /**
     * @description: 发表留言
     * @Author: runze
     * @Date: 2019/7/25 15:25
     */
    public void leaveMsg(TokenMsg user, LeavingMsgVo msgVo) {
        // 保存留言信息
        LeavingMsg entity = new LeavingMsg();
        entity.setContent(msgVo.getContent());
        entity.setUserId(user.getId());
        // 留言类型
        String type = msgVo.getType();
        entity.setType(type);
        // 文章留言
        if (ConstantUtils.MSG_TYPE_ARTICLE.equals(type)) {
            entity.setArticleId(msgVo.getArticleId());
        }
        entity.setInsertTime(new Date());
        entity.setStatus(ConstantUtils.STATUS_VALID);
        leavingMsgDao.insert(entity);
    }

    /**
     * @description: 文章预览界面展示
     * @Author: runze
     * @Date: 2019/7/25 15:29
     */
    public ViewDataVo view(Integer id) {
        ViewDataVo viewDataVo = new ViewDataVo();
        // 获取文章
        Article article = articleDao.selectById(id);
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(articleVo, article);
        viewDataVo.setArticle(articleVo);
        // 文章留言信息
        ResultPage<LeavingMsgVo> msgPage = findArticleMsgPage(id, PageParam.NO_PAGE, 6);
        viewDataVo.setMsgPage(msgPage);
        // 推荐文章
        List<ArticleVo> hots = this.findArticles(PageParam.NO_PAGE, 8, null, "views");
        viewDataVo.setHots(hots);
        return viewDataVo;
    }
}
