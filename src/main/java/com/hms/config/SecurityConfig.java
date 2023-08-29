package com.hms.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.sql.DataSource;

import java.io.IOException;
import java.util.Set;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Value("${admin.username}")
    private String adminUserName;
    @Value("${admin.password}")
    private String adminPassword;

    @Autowired
    public DataSource dataSource;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authz) -> authz
                                    .requestMatchers("/admin/**").hasRole("ADMIN")
                                    .requestMatchers("/customer/**").hasRole("CUSTOMER")
                                    .requestMatchers("/h2/**").permitAll()
                                    .anyRequest().permitAll()
                                    ).formLogin().loginPage("/login").successHandler(new CustomAuthenticationSuccessHandler()).permitAll().and().logout().logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID").permitAll();
        return http.build();
    }
    private static class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler {
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request,
                                            HttpServletResponse response, Authentication authentication) throws IOException {
            Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
            if (roles.contains("ROLE_ADMIN")) {
                System.out.println("check1");
                response.sendRedirect("/admin/home");
            } else if (roles.contains("ROLE_CUSTOMER")) {
                response.sendRedirect("/customer/home");
            } else {
                System.out.println("check2");

                response.sendRedirect("/");
            }
        }

        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
            System.out.println("authentication failed--------------------");
        }
    }

//    @Bean
//    public UserDetailsManager users(DataSource dataSource) {
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("password")
//                .roles("CUSTOMER")
//                .build();
//        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
//        users.createUser(user);
//        return users;
//    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username(adminUserName)
                .password(adminPassword)
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username=?")
                .authoritiesByUsernameQuery("SELECT username, role FROM user_roles WHERE username=?")
                .passwordEncoder(new BCryptPasswordEncoder());
        auth.inMemoryAuthentication().withUser(adminUserName).password("{noop}"+adminPassword).roles("ADMIN");
    }

}