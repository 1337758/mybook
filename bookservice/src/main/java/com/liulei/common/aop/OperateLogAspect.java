package com.liulei.common.aop;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.liulei.book.dao.OperateLogDao;
import com.liulei.book.po.OperateLog;
import com.liulei.common.annotation.Operate;
import com.liulei.common.util.ConstantUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @description 操作日志处理类
 * @auther runze
 * @date 2019/7/26 16:57
 */

@Aspect
@Configuration
public class OperateLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(OperateLogAspect.class);

    private HttpServletRequest request = null;

    @Autowired
    OperateLogDao operateLogDao;

    @Pointcut("execution(* com.liulei..*.*Controller.*(..))")
    public void operateLog() {

    }

    /**
     * 方法调用前触发 记录开始时间
     */
    @Before("operateLog()")
    public void before(JoinPoint joinPoint) {

    }

    /**
     * 环绕触发
     */
    @Around("operateLog()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Operate annotation = methodSignature.getMethod().getAnnotation(Operate.class);
        // 存在日志注解才进行方法拦截
        if (null != annotation) {
            request = getHttpServletRequest();
            String requestURI = request.getRequestURI();
            logger.info("插入操作日志:" + requestURI);
            // 保存日志信息
            OperateLog operateLog = new OperateLog();
            operateLog.setOperateDesc(annotation.value());
            // 参数
            operateLog.setRequestParams(getParams());
            operateLog.setStatus(ConstantUtils.STATUS_VALID);
            // ip
            operateLog.setOperateIp(getIp());
            // 插入操作痕迹到数据库
            operateLogDao.insert(operateLog);
        }
        return point.proceed();
    }

    /**
     * @description: 获取操作机器ip
     * @Author: runze
     * @Date: 2019/7/26 17:52
     */
    private String getIp() {
        // 获取客户端ip
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            String ips = request.getHeader("x-forwarded-for");
            if(StringUtils.isNoneBlank(ips)) {
                ip = ips.contains(",") ? ip.split(",")[0] : ips;
            }
            if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        }
        return ip;
    }

    /**
     * @description: 获取请求参数
     * @Author: runze
     * @Date: 2019/7/26 17:50
     */
    private String getParams() {
        Map<String, Object> map = new HashMap<String, Object>();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length > 0) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    map.put(paramName, paramValue);
                }
            }
        }
        return JSON.toJSONString(map);
    }

    /**
     * 获取request
     */
    private HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = sra.getRequest();
        return request;
    }

}
