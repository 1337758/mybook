package com.liulei.book.controller;

import com.liulei.book.service.ArticleService;
import com.liulei.book.vo.ArticleVo;
import com.liulei.book.vo.PageQueryVo;
import com.liulei.common.vo.ResultResponse;
import com.liulei.mybatis.util.ResultPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api
//@Token
@RestController
@RequestMapping("/art")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * @description: 查询文章分页数据
     * @Author: runze
     * @Date: 2019/7/25 15:55
     */
    @ApiOperation(value = "查询文章分页数据", notes = "")
    @PostMapping("/list")
    public ResultResponse<ResultPage<ArticleVo>> articleList(@RequestBody PageQueryVo pageQueryVo) {
        ResultResponse<ResultPage<ArticleVo>> response = new ResultResponse<>();
        try {
            ResultPage<ArticleVo> page = articleService.getPageData(pageQueryVo);
            response.setData(page);
        } catch (Exception e) {
            response.error("加载文章分页数据失败");
            e.printStackTrace();
        }
        return response;
    }

    @ApiOperation(value = "新增文章", notes = "")
    @PutMapping("/add")
    public ResultResponse<String> articleAdd(@RequestBody ArticleVo articleVo){
        ResultResponse<String> response = new ResultResponse<>();
        try {
            articleService.addArticle(articleVo);
        } catch (Exception e) {
            response.error("新增文章处理失败");
            e.printStackTrace();
        }
        return response;
    }
}
