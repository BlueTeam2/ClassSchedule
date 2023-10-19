package com.softserve.config;

import com.softserve.entity.User;
import com.softserve.exception.SocialClientRegistrationException;
import com.softserve.security.jwt.JwtConfigurer;
import com.softserve.security.jwt.JwtTokenProvider;
import com.softserve.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
@EnableOAuth2Client
@PropertySource({"classpath:social.properties", "classpath:cors.properties"})
@ComponentScan(basePackages = {"com.softserve.*"}, excludeFilters = {
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                value = {CacheConfiguration.class}
        )
})
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider jwtTokenProvider;

    private final Environment env;
    private final UserService userService;

    private static final String CLIENT_PROPERTY_KEY = "spring.security.oauth2.client.registration.";
    private static final String GOOGLE = "google";
    private static final String FACEBOOK = "facebook";

    //PRIVATE ENDPOINTS
    private static final String MANAGER_ENDPOINT = "/managers/**";
    private static final String CLASSES_ENDPOINT = "/classes/**";
    private static final String GROUPS_ENDPOINT = "/groups/**";
    private static final String LESSONS_ENDPOINT = "/lessons/**";
    private static final String ROOMS_ENDPOINT = "/rooms/**";
    private static final String SUBJECTS_ENDPOINT = "/subjects/**";
    private static final String TEACHERS_ENDPOINT = "/teachers/**";
    private static final String AUTH_ENDPOINT = "/auth/**";
    private static final String SEMESTERS_ENDPOINT = "/semesters/**";
    private static final String ROOM_TYPES_ENDPOINT = "/room-types/**";
    private static final String DEPARTMENTS_ENDPOINT = "/departments/**";

    //PUBLIC ENDPOINTS
    private static final String SCHEDULE_FOR_USERS_ENDPOINT = "/schedules/full/*";
    private static final String GROUPS_BY_SEMESTER_ID_PUBLIC_ENDPOINT = "/semesters/{semesterId}/groups";
    private static final String GROUPS_FOR_CURRENT_SEMESTER_PUBLIC_ENDPOINT = "/semesters/current/groups";
    private static final String GROUPS_FOR_DEFAULT_SEMESTER_PUBLIC_ENDPOINT = "/semesters/default/groups";
    private static final String ALL_TEACHERS_PUBLIC_ENDPOINT = "/public/teachers";
    private static final String ALL_CLASSES_PUBLIC_ENDPOINT = "/public/classes";
    private static final String ALL_SEMESTERS_PUBLIC_ENDPOINT = "/public/semesters";
    private static final String DOWNLOAD_SCHEDULE_ENDPOINT = "/download/**";
    private static final String DEFAULT_SEMESTER_PUBLIC_ENDPOINT = "/semesters/default";

    //FRONTEND ENDPOINTS
    private static final String HOME_ENDPOINT = "/";
    private static final String LOGIN_ENDPOINT = "/login";
    private static final String ADMIN_ENDPOINT = "/admin";
    private static final String FRONTEND_SCHEDULE_ENDPOINT = "/schedule";
    private static final String FRONTEND_ACTIVATION_PAGE_ENDPOINT = "/activation-page";

    //TEACHER
    private static final String GROUPS_BY_TEACHER_ID_ENDPOINT = "/groups/teacher/{teacherId}";
    private static final String GROUP_WITH_STUDENTS = "/groups/{id}/with-students";


    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider, Environment env, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.env = env;
        this.userService = userService;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(FRONTEND_ACTIVATION_PAGE_ENDPOINT, DOWNLOAD_SCHEDULE_ENDPOINT,
                        AUTH_ENDPOINT, SCHEDULE_FOR_USERS_ENDPOINT, GROUPS_BY_SEMESTER_ID_PUBLIC_ENDPOINT,
                        ALL_TEACHERS_PUBLIC_ENDPOINT, HOME_ENDPOINT, LOGIN_ENDPOINT, ADMIN_ENDPOINT,
                        FRONTEND_SCHEDULE_ENDPOINT, ALL_CLASSES_PUBLIC_ENDPOINT, ALL_SEMESTERS_PUBLIC_ENDPOINT,
                        GROUPS_FOR_DEFAULT_SEMESTER_PUBLIC_ENDPOINT, GROUPS_FOR_CURRENT_SEMESTER_PUBLIC_ENDPOINT,
                        DEFAULT_SEMESTER_PUBLIC_ENDPOINT).permitAll()
                .antMatchers(GROUPS_BY_TEACHER_ID_ENDPOINT, GROUP_WITH_STUDENTS).hasAnyRole("MANAGER", "TEACHER")
                .antMatchers(MANAGER_ENDPOINT, CLASSES_ENDPOINT, GROUPS_ENDPOINT, LESSONS_ENDPOINT,
                        ROOMS_ENDPOINT, SUBJECTS_ENDPOINT, TEACHERS_ENDPOINT, SEMESTERS_ENDPOINT,
                        ROOM_TYPES_ENDPOINT, DEPARTMENTS_ENDPOINT).hasRole("MANAGER")
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth_login")
                .and()
                .clientRegistrationRepository(clientRegistrationRepository())
                .authorizedClientService(clientService())
                .userInfoEndpoint()
                .and()
                .successHandler(authenticationSuccessHandler())
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));

        http
                .exceptionHandling()
                .authenticationEntryPoint((request, response, e) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.getWriter().write(new JSONObject()
                                    .put("message", "Access denied")
                                    .toString());
                        }
                );
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/favicon.ico",
                "/**/*.png",
                "/**/*.gif",
                "/**/*.svg",
                "/**/*.jpg",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js",
                "/**/*.json",
                "/**/*.map",
                "/assets/**",
                "/webjars/**");
    }

    @Bean
    public OAuth2AuthorizedClientService clientService() {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository());
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> registrations = Stream.of(FACEBOOK, GOOGLE)
                .map(this::getRegistration)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return new InMemoryClientRegistrationRepository(registrations);
    }

    private Optional<ClientRegistration> getRegistration(String client) {
        if (isKnownClient(client)) {
            String clientId = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-id");
            String clientSecret = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-secret");
            ClientRegistration registration = getBuilderOfClient(client)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .build();
            return Optional.of(registration);
        }
        return Optional.empty();
    }

    private boolean isKnownClient(String client) {
        return Arrays.asList(GOOGLE, FACEBOOK)
                .contains(client);
    }

    private ClientRegistration.Builder getBuilderOfClient(String client) {
        switch (client) {
            case GOOGLE:
                return CommonOAuth2Provider.GOOGLE.getBuilder(client);
            case FACEBOOK:
                return CommonOAuth2Provider.FACEBOOK.getBuilder(client);
            default:
                throw new SocialClientRegistrationException("Unknown client");
        }
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            User user = userService.createSocialUser(oAuth2User);

            String url = env.getProperty("backend.url");
            String jwtToken = jwtTokenProvider.createToken(user.getEmail(), user.getRole().toString());
            response.sendRedirect(url + "login?social=true&token=" + jwtToken);
        };
    }
}
