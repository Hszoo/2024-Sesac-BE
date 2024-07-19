package seomile.api.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/member/login", "/member/signup", "/member/get").permitAll() // 모든 사용자에게 허용
                                .anyRequest().authenticated() // 나머지 요청은 인증된 사용자만 접근 가능
                ).formLogin(formLogin ->
                        formLogin
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .permitAll()
                );
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}