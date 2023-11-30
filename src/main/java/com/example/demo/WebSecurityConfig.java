package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    MemberDetailsService memberDetailsService() {
		return new MemberDetailsService();
	}

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(memberDetailsService());
		authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
		return authenticationProvider;
	}

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                (requests) -> requests.requestMatchers(request -> request.getServletPath().equals("/")).permitAll()

                .requestMatchers(request -> request.getServletPath().equals("/category")).permitAll()
                        .requestMatchers(request -> request.getServletPath().equals("/items")).hasAnyRole("ADMIN", "USER")
                        .requestMatchers(request -> request.getServletPath().startsWith("/itemadd")).hasRole("ADMIN")
                        .requestMatchers(request -> request.getServletPath().startsWith("/itemedit")).hasRole("ADMIN")
                        .requestMatchers(request -> request.getServletPath().equals("/members")).hasRole("ADMIN")
                        .requestMatchers(request -> request.getServletPath().equals("/memberadd")).permitAll()
                        .requestMatchers(request -> request.getServletPath().equals("/membersave")).permitAll()
                        .requestMatchers(request -> request.getServletPath().equals("/cart")).hasRole("USER")
                        .requestMatchers(request -> request.getServletPath().startsWith("/member")).hasRole("ADMIN")
                        .requestMatchers(request -> request.getServletPath().startsWith("/uploads/")).permitAll()
                        .requestMatchers(request -> request.getServletPath().startsWith("/bootstrap/")).permitAll()
                        .requestMatchers(request -> request.getServletPath().startsWith("/css/")).permitAll()
                        .requestMatchers(request -> request.getServletPath().startsWith("/js/"))
                        .permitAll().requestMatchers(request -> request.getServletPath().startsWith("/images/"))
                        .permitAll().anyRequest().authenticated())
                .formLogin((form) -> form.loginPage("/login").defaultSuccessUrl("/", true)
                        .permitAll())
                .logout((logout) -> logout.logoutUrl("/logout")
                        .permitAll())
                .exceptionHandling(handling -> handling.accessDeniedPage("/403"));

		return http.build();
	}
}