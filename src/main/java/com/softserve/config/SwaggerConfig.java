package com.softserve.config;

import com.softserve.entity.CurrentUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private static final String SECURITY_REFERENCE = "Token Access";
    private static final String AUTHORIZATION = "Authorization";
    private static final String AUTHORIZATION_SCOPE = "Unlimited";
    private static final String AUTHORIZATION_DESCRIPTION = "Full Api Permission";
    private static final String API_KEY_HEADER = "Header";
    private static final String API_TITLE = "My API";
    private static final String API_VERSION = "1.0";
    private static final String API_TERMS_OF_SERVICE_URL = "Terms of service";
    private static final String API_LICENSE = "License of API";
    private static final String API_LICENSE_URL = "API license URL";
    private static final String API_DESCRIPTION = "<h3> **Note**: This API requires an 'API KEY', " +
            "please go to \"auth/sign-in\" in the Authentication Api and sign in to access your key.</h3>";
    private static final String CONTACT_NAME = "SoftServe";
    private static final String CONTACT_URL = "";
    private static final String CONTACT_EMAIL = "";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .ignoredParameterTypes(CurrentUser.class)
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Collections.singletonList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .consumes(new HashSet<>(Collections.singletonList("application/json")))
                .produces(new HashSet<>(Collections.singletonList("application/json")));
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                API_TITLE,
                API_DESCRIPTION,
                API_VERSION,
                API_TERMS_OF_SERVICE_URL,
                contact(),
                API_LICENSE,
                API_LICENSE_URL,
                Collections.emptyList());
    }

    private Contact contact() {
        return new Contact(CONTACT_NAME, CONTACT_URL, CONTACT_EMAIL);
    }

    private ApiKey apiKey() {
        return new ApiKey(SECURITY_REFERENCE, AUTHORIZATION, API_KEY_HEADER);
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(securityReferences()).build();
    }

    private List<SecurityReference> securityReferences() {
        AuthorizationScope[] authorizationScopes = {new AuthorizationScope(AUTHORIZATION_SCOPE,
                AUTHORIZATION_DESCRIPTION)};
        return Collections.singletonList(new SecurityReference(SECURITY_REFERENCE, authorizationScopes));
    }
}
