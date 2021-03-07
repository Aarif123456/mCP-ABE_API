package com.phr.cpabe;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.util.Properties;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {
        "com.phr.cpabe.TrustedAuthority",
        "com.phr.cpabe.Attributes",
        "com.phr.cpabe.DataOwner",
        "com.phr.cpabe.RevocationServer",
        //"com.phr.cpabe.AuthorityServer",
        "com.phr.cpabe.Users"
})
public class WebAppContext implements WebMvcConfigurer {
    // Get web files from static folder
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
        registry.addResourceHandler(
                "/img/**",
                "/scripts/**",
                "/styles/**")
                .addResourceLocations(
                        "classpath:/static/img/",
                        "classpath:/static/scripts/",
                        "classpath:/static/styles/");


    }
//    @Bean
//    public ScriptTemplateConfigurer configurer() {
//        ScriptTemplateConfigurer configurer = new ScriptTemplateConfigurer();
//
//
//        configurer.setScripts("/static/scripts/loadPatient.js");
//
//
//        return configurer;
//    }
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public SimpleMappingExceptionResolver exceptionResolver() {
        SimpleMappingExceptionResolver exceptionResolver = new SimpleMappingExceptionResolver();

        Properties exceptionMappings = new Properties();

//        exceptionMappings.put("com.phr.cpabe.testmvc.exception.TodoNotFoundException", "error/404");
        exceptionMappings.put("java.lang.Exception", "error/error");
        exceptionMappings.put("java.lang.RuntimeException", "error/error");

        exceptionResolver.setExceptionMappings(exceptionMappings);

        Properties statusCodes = new Properties();

        statusCodes.put("error/404", "404");
        statusCodes.put("error/error", "500");

        exceptionResolver.setStatusCodes(statusCodes);

        return exceptionResolver;
    }


//    @Bean
//    public ViewResolver viewResolver() {
//        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
//
//        viewResolver.setViewClass(JstlView.class);
//        viewResolver.setPrefix("/WEB-INF/jsp/");
//        viewResolver.setSuffix(".jsp");
//
//        return viewResolver;
//    }
}