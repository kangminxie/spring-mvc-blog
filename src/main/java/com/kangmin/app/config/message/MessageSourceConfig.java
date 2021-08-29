package com.kangmin.app.config.message;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class MessageSourceConfig {

    private static final String MESSAGE_PROPERTIES_DIRECTORY = "classpath:i18n/messages";

    @Bean(name = "messageSource")
    public MessageSource messageSource() {
        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename(MESSAGE_PROPERTIES_DIRECTORY);
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
