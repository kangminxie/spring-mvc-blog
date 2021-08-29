package com.kangmin.app.interceptor;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

public class RequestIpLogInterceptor implements HandlerInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(RequestIpLogInterceptor.class);

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) {
        LOG.info("[preHandle][" + request.getMethod() + "][" + request.getRequestURI() + getParameters(request) + "]");
        return true;
    }

    @Override
    public void postHandle(final HttpServletRequest request,
                           final HttpServletResponse response,
                           final Object handler, final ModelAndView modelAndView) {
        // LOG.info("[postHandle][" + request.getMethod() + "][" + request.getRequestURI() + getParameters(request) + "]");
    }

    @Override
    public void afterCompletion(final HttpServletRequest request,
                                final HttpServletResponse response,
                                final Object handler,
                                final Exception ex) {
        if (ex != null) {
            ex.printStackTrace();
        }
        // LOG.info("[afterCompletion][" + request.getMethod() + "][" + request.getRequestURI()
        //        + "][Exception: " + ex + "]");
    }

    private String getParameters(final HttpServletRequest request) {
        final StringBuilder posted = new StringBuilder();
        final Enumeration<?> parameterNames = request.getParameterNames();
        if (parameterNames != null) {
            posted.append("?");
        }
        while (parameterNames != null && parameterNames.hasMoreElements()) {
            if (posted.length() > 1) {
                posted.append("&");
            }
            final String curr = (String) parameterNames.nextElement();
            posted.append(curr).append("=");
            if (curr.contains("password") || curr.contains("answer") || curr.contains("pwd")) {
                posted.append("*****");
            } else {
                posted.append(request.getParameter(curr));
            }
        }

        final String ip = request.getHeader("X-FORWARDED-FOR");
        final String ipAddr;
        if (ip == null) {
            ipAddr = getRemoteAddr(request);
        } else {
            ipAddr = ip;
        }
        if (!Strings.isNullOrEmpty(ipAddr)) {
            // Program and System Information Protocol (psip)
            posted.append("&_psip=").append(ipAddr);
        }
        return posted.toString();
    }

    private String getRemoteAddr(final HttpServletRequest request) {
        final String ipFromHeader = request.getHeader("X-FORWARDED-FOR");
        if (ipFromHeader != null && ipFromHeader.length() > 0) {
            LOG.debug("ip from proxy - X-FORWARDED-FOR : " + ipFromHeader);
            return ipFromHeader;
        }
        return request.getRemoteAddr();
    }
}
