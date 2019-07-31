package com.liulei.common.Controller;

import com.alibaba.fastjson.JSONException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @description
 * @auther runze
 * @date 2019/7/29 11:03
 */

@RestController
@RequestMapping("/ueditor")
public class UeditorController {

    /**
     * @description: editor插件加载图片
     * @Author: runze
     * @Date: 2019/7/29 11:03
     */
    @RequestMapping("/img")
    @ResponseBody
    public void getConfigInfo(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        String rootPath = request.getSession().getServletContext().getRealPath("/");
        try {
            String exec = new ActionEnter(request, rootPath).exec();
            PrintWriter writer = response.getWriter();
            writer.write(exec);
            writer.flush();
            writer.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

}
