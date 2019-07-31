package com.liulei.common.filter;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 防止XSS sql注入攻击
 *urlPatterns 配置要拦截的资源
 * urlPatterns = "/*"  通配符，拦截所有web资源
 */
@Order(1)
@Configuration
@WebFilter(filterName = "XSSFilter",urlPatterns = "/*")
public class XSSFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		/*过滤方法 主要是对request和response进行一些处理，然后交给下一个过滤器或Servlet处理*/
		chain.doFilter(new XSSRequestWrapper((HttpServletRequest) request), response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
