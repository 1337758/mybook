package com.liulei.book.controller;

import com.liulei.book.service.ConstService;
import com.liulei.book.vo.ConstVo;
import com.liulei.book.vo.PageQueryVo;
import com.liulei.common.vo.ResultResponse;
import com.liulei.mybatis.util.ResultPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description
 * @auther runze
 * @date 2019/7/25 15:59
 */

@Api
@RestController
@RequestMapping("/const")
public class ConstController {

    @Autowired
    private ConstService constService;

    @ApiOperation(value = "查询常量表分页数据", notes = "")
    @PostMapping("/list")
    public ResultResponse<ResultPage<ConstVo>> articleList(@RequestBody PageQueryVo pageQueryVo) {
        ResultResponse<ResultPage<ConstVo>> response = new ResultResponse<>();
        try {
            ResultPage<ConstVo> page = constService.getPageData(pageQueryVo);
            response.setData(page);
        } catch (Exception e) {
            response.error("加载常量表分页失败");
            e.printStackTrace();
        }
        return response;
    }

    @ApiOperation(value = "新增常量", notes = "")
    @PutMapping("/add")
    public ResultResponse<String> articleAdd(@RequestBody ConstVo constVo) {
        ResultResponse<String> response = new ResultResponse<>();
        try {
            constService.addConst(constVo);
        } catch (Exception e) {
            response.error("新增常量处理失败");
            e.printStackTrace();
        }
        return response;
    }

    @ApiOperation(value = "获取文章类型和分类", notes = "")
    @GetMapping("/artTypes")
    public ResultResponse<List<ConstVo>> artTypes() {
        ResultResponse<List<ConstVo>> response = new ResultResponse<>();
        try {
            List<ConstVo> types = constService.getArticleTypes();
            response.setData(types);
        } catch (Exception e) {
            response.error("获取文章类型和分类");
            e.printStackTrace();
        }
        return response;
    }


    @ApiOperation(value = "获取文章类型和分类", notes = "")
    @GetMapping("/allTypes")
    public ResultResponse<List<ConstVo>> allTypes() {
        ResultResponse<List<ConstVo>> response = new ResultResponse<>();
        try {
            List<ConstVo> types = constService.getAllArticleTypes("文章类型");
            response.setData(types);
        } catch (Exception e) {
            response.error("获取文章类型和分类");
            e.printStackTrace();
        }
        return response;
    }

    @ApiOperation(value = "根据id删除常量", notes = "")
    @DeleteMapping("/{id}")
    public ResultResponse<String> delConst(@PathVariable Integer id) {
        ResultResponse<String> response = new ResultResponse<>();
        try {
            constService.delConst(id);
        } catch (Exception e) {
            response.error("删除常量失败");
            e.printStackTrace();
        }
        return response;
    }

}
