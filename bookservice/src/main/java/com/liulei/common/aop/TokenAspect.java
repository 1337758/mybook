package com.liulei.common.aop;

import com.alibaba.fastjson.JSON;
import com.liulei.common.annotation.NoToken;
import com.liulei.common.annotation.Token;
import com.liulei.common.util.JwtUtils;
import com.liulei.common.vo.ResultResponse;
import com.liulei.common.vo.TokenMsg;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @description token处理切面
 * @auther runze
 * @date 2019/7/29 10:05
 */

@Slf4j
@Aspect
@Configuration
public class TokenAspect {

    private HttpServletRequest request = null;

    @Pointcut("execution(* com.liulei..*.*Controller.*(..))")
    public void tokenLog() {

    }

    /**
     * @description: 环绕触发
     * @Author: runze
     * @Date: 2019/7/29 10:12
     */
    @Around("tokenLog()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        //是否需要token --需要token：1.类上Token注解，方法上没有NoToken 2.方法上Token注解
        boolean isTokenNeed = false;
        Class<?> aClass = point.getTarget().getClass();
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        // 判断类上没有Token注解
        if (aClass.getAnnotation(Token.class) != null) {
            // 判断方法上是否有NoToken注解或者Token注解
            isTokenNeed = method.getAnnotation(NoToken.class) == null || method.getAnnotation(Token.class) != null;
        } else {
            // 判断方法上是否有NoToken注解或者Token注解
            isTokenNeed = method.getAnnotation(Token.class) != null;
        }
        if (isTokenNeed) {
            request = getHttpServletRequest();
            String token = request.getParameter("token");
            log.info("TokenAspect--token：" + token);
            //校验token
            TokenMsg tokenMsg = JwtUtils.parse(TokenMsg.class, token);
            if (null != tokenMsg) {
                request.setAttribute("user", tokenMsg);
            } else {
                //token解析失败，则直接返回失败信息
                HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
                ServletOutputStream os = response.getOutputStream();
                ResultResponse<String> api = new ResultResponse<String>();
                api.errorToken();
                String msg = JSON.toJSONString(api);
                os.write(msg.getBytes("UTF-8"));
                return null;
            }
        }
        return point.proceed();
    }


    /**
     * @description: 获取request
     * @Author: runze
     * @Date: 2019/7/29 10:20
     */
    private HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = sra.getRequest();
        return request;
    }

}
