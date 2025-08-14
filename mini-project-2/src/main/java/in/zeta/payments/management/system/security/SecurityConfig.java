package in.zeta.payments.management.system.security;

import in.zeta.payments.management.system.enums.Role;
import in.zeta.payments.management.system.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private static final String ROLE_ADMIN = Role.ADMIN.name();
    private static final String USER_BY_ID_PATH = "/pms-api/users/{userID}";
    private static final String USER_PATH = "/pms-api/users";


    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider, CustomUserDetailsService customUserDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy(
                "ROLE_ADMIN > ROLE_FINANCE_MANAGER\n" +
                        "ROLE_FINANCE_MANAGER > ROLE_VIEWER"
        );
    }

    @Bean
    public DefaultMethodSecurityExpressionHandler expressionHandler(RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy);
        return handler;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider, customUserDetailsService);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

     @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                 .csrf(AbstractHttpConfigurer::disable)
                 .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                 .authorizeHttpRequests(this::configureAuthorization)
                 .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

         return http.build();
     }

    private void configureAuthorization(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizationManagerRequestMatcherRegistry) {
        authorizationManagerRequestMatcherRegistry
                .requestMatchers("/pms-api/users/login").permitAll()
                .requestMatchers(HttpMethod.GET,USER_PATH).hasRole(ROLE_ADMIN)
                .requestMatchers(HttpMethod.GET,USER_BY_ID_PATH).hasRole(ROLE_ADMIN)
                .requestMatchers(HttpMethod.POST,USER_PATH).hasRole(ROLE_ADMIN)
                .requestMatchers(HttpMethod.PATCH, USER_BY_ID_PATH).hasRole(ROLE_ADMIN)
                .requestMatchers(HttpMethod.DELETE,USER_BY_ID_PATH).hasRole(ROLE_ADMIN)
                .anyRequest().authenticated();
    }


}

