package com.dong.tech.config;

import com.dong.tech.config.jwt.JwtAccessDeniedHandler;
import com.dong.tech.config.jwt.JwtAuthenticationEntryPoint;
import com.dong.tech.config.jwt.TokenProvider;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * The Class SecurityConfig.
 *
 * @author dongsulee
 * @date 2023/09/25
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    /**
     * @param httpSecurity
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .csrf().disable() // token 사용하기 때문에 disable
                .cors().disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                // enable h2-console
//                .and()
//                .headers()
//                .frameOptions()
//                .sameOrigin()

                // 세션을 사용하지 않기 때문에 STATELESS로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeHttpRequests(request -> request
                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                        .requestMatchers("/view/login","/view/join","/auth/login","/auth/join").permitAll()
                        .anyRequest().authenticated()	// 어떠한 요청이라도 인증필요
                )
//                .formLogin(login -> login	// form 방식 로그인 사용
//                        .loginPage("/view/login")
//                        .loginProcessingUrl("/login-process")
//                        .usernameParameter("id")
//                        .passwordParameter("pwd")
//                        .defaultSuccessUrl("/user", true)	// 성공 시 dashboard로
//                        .permitAll()	// 대시보드 이동이 막히면 안되므로 얘는 허용
//                )
                .apply(new JwtSecurityConfig(tokenProvider))
                .and()
//                .addFilterBefore(userAuthenticationFilter(),
//                        UsernamePasswordAuthenticationFilter.class)
                .logout(Customizer.withDefaults());	// 로그아웃은 기본설정으로 (/logout으로 인증해제)
        return httpSecurity.build();
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
