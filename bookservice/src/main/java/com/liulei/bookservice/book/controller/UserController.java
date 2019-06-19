package com.liulei.bookservice.book.controller;

import com.liulei.bookservice.book.service.UserService;
import com.liulei.bookservice.book.vo.BlogUserVo;
import com.liulei.bookservice.common.util.JwtUtils;
import com.liulei.bookservice.common.vo.ResultResponse;
import com.liulei.bookservice.common.vo.TokenMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户登录", notes = "")
    @RequestMapping("/login")
    public ResultResponse<BlogUserVo> login(@RequestBody BlogUserVo blogUserVo) {

        ResultResponse<Object> response = new ResultResponse<>();

        BlogUserVo user = userService.userLogin(blogUserVo);
        if (null != user) {
            response.setData(user);
            //返回token
            response.setToken(JwtUtils.sign(new TokenMsg().setInfo(user)));
        }

        return null;
    }

}
