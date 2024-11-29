package org.example.config;

import org.example.repos.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserService userService;
    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }
    @Bean // шифрование пароля
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);  // Здесь передаем ваш UserService
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    @Bean // настройки аунтефикации
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeRequests(auth -> { // доступ для не зарегестрированного юзера
                    auth.antMatchers(
                            "/", "/index", "/login", "/js/**",
                            "/register", "/css/**", "/property", "/icon/**", "/api/**"
                    ).permitAll();
                    auth.antMatchers("/addProperty").hasRole("ADMIN");
                    auth.anyRequest().authenticated();
                })
                .formLogin(form -> form // обработка post запроса из...
                        .loginPage("/login")
                        .usernameParameter("name")
                        .loginProcessingUrl("/login")
                        .permitAll()
                        .defaultSuccessUrl("/", true)
                ) //
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .authenticationProvider(authenticationProvider());
        return http.build();
    }
}