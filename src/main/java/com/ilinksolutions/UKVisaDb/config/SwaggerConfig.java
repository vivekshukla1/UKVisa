package com.ilinksolutions.UKVisaDb.config;


import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

	@Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ilinksolutions.UKVisaDb.rservices"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaInfo());
    }

    private ApiInfo metaInfo() {

        ApiInfo apiInfo = new ApiInfo(
                "UK Visa API",
                "This service is used for testing the UK Visa Rest APIs",
                "1.0",
                "Terms of Service",
                new Contact("I-Link Solutions, Inc.", "http://ilinksolution.com/",
                        "junaid.qureshi@ilinksolution.com"),
                "I-Link Solutions, Inc. License Version 2.0",
                "http://ilinksolution.com/", Collections.emptyList()
        );

        return apiInfo;
    }

}
