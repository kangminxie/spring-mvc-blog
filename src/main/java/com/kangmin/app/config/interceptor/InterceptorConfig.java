package com.kangmin.app.config.interceptor;

import com.kangmin.app.interceptor.ProcessingTimeLogInterceptor;
import com.kangmin.app.interceptor.RequestIpLogInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class InterceptorConfig {

    private static final Logger LOG = LoggerFactory.getLogger(InterceptorConfig.class);

    @Value("${application.locale:}")
    private String applicationLocale;

    @Bean
    public ProcessingTimeLogInterceptor processingTimeLogInterceptor() {
        return new ProcessingTimeLogInterceptor();
    }

    @Bean
    public RequestIpLogInterceptor loggerInterceptor() {
        return new RequestIpLogInterceptor();
    }

    // == this bean is needed for Spring LocaleChangeInterceptor below ==
    @Bean
    public LocaleResolver localeResolver() {
        final SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(getLocale());
        // resolver.setDefaultLocale(new Locale("en"));
        return resolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        final LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("language");
        return localeChangeInterceptor;
    }

    private Locale getLocale() {
        if (applicationLocale.isEmpty()) {
            LOG.info("*** applicationLocale.isEmpty()");
            return Locale.US;
        }

        LOG.debug(">>> applicationLocale is: " + applicationLocale);
        switch (applicationLocale) {
            case "us":
            case "en":
                return Locale.ENGLISH;
            case "cn":
                return Locale.CHINESE;
            case "ca":
                return Locale.CANADA;
            default:
                return Locale.US;
        }
    }
}
