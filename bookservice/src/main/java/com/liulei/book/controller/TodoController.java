package com.liulei.book.controller;

import com.liulei.book.service.TodoService;
import com.liulei.book.vo.PageQueryVo;
import com.liulei.book.vo.TodoVo;
import com.liulei.common.vo.ResultResponse;
import com.liulei.mybatis.util.ResultPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description
 * @auther runze
 * @date 2019/7/25 16:19
 */
@Api
@RestController
@RequestMapping("/todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @ApiOperation(value = "查询待办任务表分页数据", notes = "")
    @PostMapping("/list")
    public ResultResponse<ResultPage<TodoVo>> articleList(@RequestBody PageQueryVo pageQueryVo) {
        ResultResponse<ResultPage<TodoVo>> response = new ResultResponse<>();
        try {
            ResultPage<TodoVo> page = todoService.getPageData(pageQueryVo);
            response.setData(page);
        } catch (Exception e) {
            response.error("加载待办任务表分页失败");
            e.printStackTrace();
        }
        return response;
    }

    @ApiOperation(value = "新增待办任务", notes = "")
    @PutMapping("/add")
    public ResultResponse<String> articleAdd(@RequestBody TodoVo todoVo) {
        ResultResponse<String> response = new ResultResponse<>();
        try {
            todoService.addTodo(todoVo);
        } catch (Exception e) {
            response.error("新增待办任务处理失败");
            e.printStackTrace();
        }
        return response;
    }

    @ApiOperation(value = "根据id删除待办任务", notes = "")
    @DeleteMapping("/{id}")
    public ResultResponse<String> delTodo(@PathVariable Integer id) {
        ResultResponse<String> response = new ResultResponse<>();
        try {
            todoService.delTodo(id);
        } catch (Exception e) {
            response.error("删除待办任务失败");
            e.printStackTrace();
        }
        return response;
    }


}
