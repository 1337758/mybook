package com.liulei.book.controller;

import com.liulei.book.po.User;
import com.liulei.book.service.BlogService;
import com.liulei.book.service.UserService;
import com.liulei.book.vo.*;
import com.liulei.common.annotation.Operate;
import com.liulei.common.annotation.Token;
import com.liulei.common.config.ServerConfig;
import com.liulei.common.exception.CustomUnauthorizedException;
import com.liulei.common.util.JwtUtils;
import com.liulei.common.vo.ResultResponse;
import com.liulei.common.vo.TokenMsg;
import com.liulei.mybatis.util.ResultPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Api
@RestController
@RequestMapping("/blog")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private ServerConfig serverConfig;

    /**
     * j接口调用测试
     *
     * @return
     */
    @GetMapping("log")
    public String log() {
        log.info("这是info信息2");
        log.error("这是error信息");
        return "success";
    }

    @GetMapping("/getUser")
    public ResultResponse<BlogUserVo> getUser(@RequestParam(value = "userCode") String userCode) {
        ResultResponse<BlogUserVo> response = new ResultResponse<>();
        User user = userService.getUser(userCode);
        response.setMsg(user.toString());
        return response;
    }

    /**
     * 用户登录
     *
     * @param blogUserVo
     * @return
     */
    @ApiOperation(value = "用户登录", notes = "")
    @RequestMapping("/login")
    public ResultResponse<BlogUserVo> login(@Valid @RequestBody BlogUserVo blogUserVo) {

        ResultResponse<BlogUserVo> response = new ResultResponse<>();

        BlogUserVo user = userService.userLogin(blogUserVo);
        if (null != user) {
            response.setData(user);
            //返回token
            response.setToken(JwtUtils.sign(new TokenMsg().setInfo(user)));
        } else {
            response.error("input is invalied");
        }
        return response;
    }

    /**
     * 用户注册
     *
     * @param blogUserVo
     * @return
     */
    @ApiOperation(value = "用户注册", notes = "")
    @PostMapping("/register")
    public ResultResponse<String> register(@Valid @RequestBody BlogUserVo blogUserVo) {
        ResultResponse<String> response = new ResultResponse<>();
        int userId = userService.userRegist(blogUserVo);
        if (StringUtils.isEmpty(userId)) {
            response.error("register failed");
        } else {
            response.setData("regist success");
        }
        return response;
    }

    /**
     * userCode校验
     *
     * @param userCode
     * @return
     */
    @ApiOperation(value = "用户名校验", notes = "")
    @PostMapping("/checkCode")
    public ResultResponse checkCode(@RequestParam(value = "userCode") String userCode) {
        if (StringUtils.isEmpty(userCode)) {
            throw new CustomUnauthorizedException("input is null");
        }

        ResultResponse<Object> response = new ResultResponse<>();
        Boolean exit = userService.checkCode(userCode);
        if (exit) {
            response.setData("input userCode is exit");
        }
        return response;
    }

    /**
     * 博客首页数据初始化
     *
     * @return
     */
    @ApiOperation(value = "博客首页数据初始化", notes = "")
    @GetMapping("/homeInit")
    public ResultResponse homeInit() {
        ResultResponse<Object> response = new ResultResponse<>();
        HomeDataVo homeDataVo = blogService.getHomeData();
        homeDataVo.setServerPath(serverConfig.getUrl());
        response.setData(homeDataVo);
        return response;
    }

    /**
     * 学习笔记页面数据初始化
     *
     * @return
     */
    @ApiOperation(value = "学习笔记页面数据初始化", notes = "")
    @GetMapping("/studyInit")
    public ResultResponse<StudyDataVo> studyInit() {
        ResultResponse<StudyDataVo> response = new ResultResponse<>();
        try {
            StudyDataVo studyDataVo = blogService.getStudyData();
            studyDataVo.setServerPath(serverConfig.getUrl());
            response.setData(studyDataVo);
        } catch (Exception e) {
            response.error("加载学习笔记数据失败");
            e.printStackTrace();
        }
        return response;
    }


    @ApiOperation(value = "心情随笔页面数据初始化", notes = "")
    @GetMapping("/essayInit")
    public ResultResponse<EssayDataVo> essayInit() {
        ResultResponse<EssayDataVo> response = new ResultResponse<>();
        try {
            EssayDataVo essayDataVo = blogService.getEssayData();
            essayDataVo.setServerPath(serverConfig.getUrl());
            response.setData(essayDataVo);
        } catch (Exception e) {
            response.error("加载心情随笔数据失败");
            e.printStackTrace();
        }
        return response;
    }

    @ApiOperation(value = "查询文章分页数据", notes = "")
    @PostMapping("/articlePage")
    public ResultResponse<ResultPage<ArticleVo>> articlePage(@RequestBody PageQueryVo pageQueryVo) {
        ResultResponse<ResultPage<ArticleVo>> response = new ResultResponse<>();
        try {
            ResultPage<ArticleVo> page = blogService.getPageData(pageQueryVo);
            response.setData(page);
        } catch (Exception e) {
            response.error("加载文章分页数据失败");
            e.printStackTrace();
        }
        return response;
    }

    @Operate("查看留言信息")
    @ApiOperation(value = "留言板页面数据初始化", notes = "")
    @GetMapping("/boardInit")
    public ResultResponse<BoardDataVo> boardInit() {
        ResultResponse<BoardDataVo> response = new ResultResponse<>();
        try {
            BoardDataVo boardDataVo = blogService.getBoardData();
            response.setData(boardDataVo);
        } catch (Exception e) {
            response.error("加载留言板数据失败");
            e.printStackTrace();
        }
        return response;
    }

    @ApiOperation(value = "查询留言分页数据", notes = "")
    @PostMapping("/msgPage")
    public ResultResponse<ResultPage<LeavingMsgVo>> msgPage(@RequestBody PageQueryVo pageQueryVo) {
        ResultResponse<ResultPage<LeavingMsgVo>> response = new ResultResponse<>();
        try {
            ResultPage<LeavingMsgVo> page = blogService.findMsgPage(pageQueryVo.getPageNo(), 6);
            response.setData(page);
        } catch (Exception e) {
            response.error("加载留言分页数据失败");
            e.printStackTrace();
        }
        return response;
    }

    @ApiOperation(value = "查询文章留言分页数据", notes = "")
    @PostMapping("/artMsgPage")
    public ResultResponse<ResultPage<LeavingMsgVo>> artMsgPage(@RequestBody PageQueryVo pageQueryVo) {
        ResultResponse<ResultPage<LeavingMsgVo>> response = new ResultResponse<>();
        try {
            ResultPage<LeavingMsgVo> page = blogService.findArticleMsgPage(pageQueryVo.getArticleId(), pageQueryVo.getPageNo(), 6);
            response.setData(page);
        } catch (Exception e) {
            response.error("加载文章留言分页数据失败");
            e.printStackTrace();
        }
        return response;
    }

    @ApiOperation(value = "发表留言", notes = "")
    @PostMapping("/leaveMsg")
    @Token
    public ResultResponse<String> msgPage(@RequestBody LeavingMsgVo msgVo, HttpServletRequest request) {
        ResultResponse<String> response = new ResultResponse<>();
        TokenMsg user = (TokenMsg) request.getAttribute("user");
        try {
            blogService.leaveMsg(user, msgVo);
        } catch (Exception e) {
            response.error("加载留言分页数据失败");
            e.printStackTrace();
        }
        return response;
    }

    @ApiOperation(value = "文章浏览界面", notes = "")
    @GetMapping("/view")
    public ResultResponse<ViewDataVo> view(@RequestParam(value = "id") Integer id) {
        ResultResponse<ViewDataVo> response = new ResultResponse<>();
        try {
            ViewDataVo viewDataVo = blogService.view(id);
            response.setData(viewDataVo);
        } catch (Exception e) {
            response.error("文章浏览加载失败");
            e.printStackTrace();
        }
        return response;
    }

}
