package com.kangmin.app.controller;

import com.kangmin.app.exception.WebResourceNotFoundException;
import com.kangmin.app.exception.WebUserUnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Controller
public class ExceptionController implements ErrorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionController.class);

    private static final String ERROR_TEMPLATES_PATH = "error/";

    @RequestMapping("/error")
    public ModelAndView handleError(final HttpServletRequest request) {
        final Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        final ModelAndView mv = new ModelAndView();

        String forwardedReqUrl = "/";
        String errorMessage = "";
        String subViewName = "error";
        if (status != null) {
            final int statusCode = Integer.parseInt(status.toString());
            // LOGGER.info(">>>> handleError in LocalErrorController for status: " + statusCode);
            // LOGGER.info(">>>> handleError in LocalErrorController for uri: " + request.getRequestURI()); // /error
            forwardedReqUrl = (String) request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                errorMessage = "No resource was linked with url = " + forwardedReqUrl;
                subViewName = "404";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                errorMessage = "Server was unable to handle request url = " + forwardedReqUrl;
                subViewName = "500";
            } else {
                errorMessage = "There is issue for request url = " + forwardedReqUrl;
                // subViewName = "403";
            }
        }
        mv.addObject("url", forwardedReqUrl);
        mv.addObject("exceptionMessage", errorMessage);
        mv.setViewName(ERROR_TEMPLATES_PATH + subViewName);
        return mv;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView noHandlerExceptionHandler(
        final HttpServletRequest request,
        final NoHandlerFoundException ex
    ) {
        LOGGER.info("Exception: NoHandlerFoundException was caught with Request URL: {}", request.getRequestURL());
        final ModelAndView mv = new ModelAndView();
        mv.addObject("url", request.getRequestURL());
        mv.addObject("exceptionMessage", ex.getMessage());
        mv.setViewName(ERROR_TEMPLATES_PATH + "404");
        return mv;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView generalExceptionHandler(
        final HttpServletRequest request,
        final Exception e
    ) throws Exception {
        LOGGER.info("General Exception: {} was caught with Request URL: {}", e, request.getRequestURL());
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }
        final ModelAndView mv = new ModelAndView();
        mv.addObject("url", request.getRequestURL());
        mv.addObject("exceptionMessage", e.getMessage());
        mv.setViewName(ERROR_TEMPLATES_PATH + getErrorViewName(e));
        return mv;
    }

    private String getErrorViewName(final Exception e) {
        if (e instanceof WebResourceNotFoundException) {
            return "404";
        } else if (e instanceof WebUserUnauthorizedException) {
            return "403";
        }
        return "error";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
