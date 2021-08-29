package com.kangmin.app.config;

import com.kangmin.app.config.interceptor.InterceptorConfig;
import com.kangmin.app.config.message.MessageSourceConfig;
import com.kangmin.app.interceptor.ProcessingTimeLogInterceptor;
import com.kangmin.app.interceptor.RequestIpLogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Import({
        InterceptorConfig.class,
        MessageSourceConfig.class,
})
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private RequestIpLogInterceptor requestIpLogInterceptor;
    private ProcessingTimeLogInterceptor processingTimeLogInterceptor;
    private LocaleChangeInterceptor localeChangeInterceptor;
//    private MessageSource messageSource;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {

        // == order matters ==
        registry.addInterceptor(processingTimeLogInterceptor)
                .excludePathPatterns(
                        "/css/**",
                        "/js/**",
                        "/img/**",
                        "/lib/**",
                        "/resources/**",
                        "/favicon",
                        "/webjars/**"
                );

        registry.addInterceptor(requestIpLogInterceptor)
                .excludePathPatterns(
                        "/css/**",
                        "/js/**",
                        "/img/**",
                        "/lib/**",
                        "/resources/**",
                        "/favicon",
                        "/webjars/**"
                );

        registry.addInterceptor(localeChangeInterceptor);
    }

//    @Override
//    public Validator getValidator() {
//        final LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
//        validator.setValidationMessageSource(messageSource);
//        return validator;
//    }

//    @Autowired
//    void setMessageSource(final MessageSource messageSource) {
//        this.messageSource = messageSource;
//    }

    @Autowired
    void setRequestIpLogInterceptor(final RequestIpLogInterceptor requestIpLogInterceptor) {
        this.requestIpLogInterceptor = requestIpLogInterceptor;
    }

    @Autowired
    void setProcessingTimeLogInterceptor(final ProcessingTimeLogInterceptor processingTimeLogInterceptor) {
        this.processingTimeLogInterceptor = processingTimeLogInterceptor;
    }

    @Autowired
    void setLocaleChangeInterceptor(final LocaleChangeInterceptor localeChangeInterceptor) {
        this.localeChangeInterceptor = localeChangeInterceptor;
    }
}
