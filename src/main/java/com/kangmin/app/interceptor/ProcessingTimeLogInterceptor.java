package com.kangmin.app.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProcessingTimeLogInterceptor implements HandlerInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessingTimeLogInterceptor.class);

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) {
        final long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        return true;
    }

    @Override
    public void postHandle(final HttpServletRequest request,
                           final HttpServletResponse response,
                           final Object handler,
                           @Nullable final ModelAndView modelAndView) {
        final String queryString;
        if (request.getQueryString() == null) {
            queryString = "";
        } else {
            queryString = "?" + request.getQueryString();
        }
        final String path = request.getRequestURL() + queryString;
        final long startTime = (Long) request.getAttribute("startTime");
        final long endTime = System.currentTimeMillis();
        LOG.info(String.format("[%s millisecond taken to process the request %s]", (endTime - startTime), path));
    }

    @Override
    public void afterCompletion(final HttpServletRequest request,
                                final HttpServletResponse response,
                                final Object handler,
                                @Nullable final Exception exceptionIfAny) {
        // LOG.info("[afterCompletion] at Processing ProcessingTimeLogInterceptor");
    }
}
