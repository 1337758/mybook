package com.liulei.bookservice.book.controller;

import com.liulei.bookservice.book.service.UserService;
import com.liulei.bookservice.book.vo.BlogUserVo;
import com.liulei.bookservice.common.exception.CustomUnauthorizedException;
import com.liulei.bookservice.common.util.JwtUtils;
import com.liulei.bookservice.common.vo.ResultResponse;
import com.liulei.bookservice.common.vo.TokenMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

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


}
