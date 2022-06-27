package com.sinanduman.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@Order(2)
public class Swagger2Configuration {

    @Bean
    public Docket api() {

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiEndPointsInfo())
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(List.of(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sinanduman.library.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder()
                .title("Library API")
                .description("[ Base URL /library/api/v1 ]")
                .contact(new Contact("Sinan Duman", "http://www.github.com/sinanduman", "sinanduman@gmail.com"))
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .version("1.0")
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private AuthorizationScope[] jwtAuthorizationScopes() {
        return new AuthorizationScope[]{
                new AuthorizationScope("Global", "Access Everything")
                /*
                new AuthorizationScope("lib.bm.c", "Grant user a permission to create a book"),
                new AuthorizationScope("lib.bm.r", "Grant user a permission to query books"),
                new AuthorizationScope("lib.bm.d", "Grant user a permission to delete a book"),
                new AuthorizationScope("lib.m.c", "Grant user a permission to create a member"),
                new AuthorizationScope("lib.m.r", "Grant user a permission to query a member"),
                new AuthorizationScope("lib.m.r.all", "Grant user a permission to query all members"),
                new AuthorizationScope("lib.m.d", "Grant user a permission to delete a member"),
                new AuthorizationScope("lib.bo.c", "Grant user a permission to borrow a book"),
                new AuthorizationScope("lib.bo.r", "Grant user a permission to query borrowings"),
                new AuthorizationScope("lib.bo.r.all", "Grant user a permission to query borrowings for all users"),
                new AuthorizationScope("lib.bo.return", "Grant user a permission to return a book")
                 */
        };
    }

    private List<SecurityReference> defaultAuth() {
        return List.of(new SecurityReference("JWT", jwtAuthorizationScopes()));
    }
}
