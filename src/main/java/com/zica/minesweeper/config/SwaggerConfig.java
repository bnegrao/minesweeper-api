package com.zica.minesweeper.config;

import com.zica.minesweeper.RestfulApiApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(RestfulApiApplication.class.getPackageName()))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "Minesweeper API",
                "This API let's you play minesweeper storing the state of your game. The game will be saved until it is finished (game lost or game won)",
                "1.0",
                "terms_of_service",
                new Contact("Bruno Zica","https://www.linkedin.com/in/bruno-zica-6539536/","protected@protected.com"),
                "LICENSE",
                "LICENSE URL",
                Collections.emptyList()
        );
    }
}