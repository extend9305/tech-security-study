package com.dong.tech.config.filter;

import com.dong.tech.config.jwt.TokenProvider;
import com.dong.tech.domain.Member;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;


/**
 * The Class JwtFilter.
 *
 * @author dongsulee
 */
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    public static final String AUTHORIATION_HEADER = "Authorization";
    public static final String REFRESHTOKEN_HEADER = "RefreshToken";
    private final TokenProvider tokenProvider;
    // 실제 필터릴 로직
    // 토큰의 인증정보를 SecurityContext에 저장하는 역할 수행
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String accessToken = resolveToken(httpServletRequest,AUTHORIATION_HEADER);
        String refreshToken = resolveToken(httpServletRequest,REFRESHTOKEN_HEADER);


        String requestURI = httpServletRequest.getRequestURI();

        if(StringUtils.hasText(accessToken)){

            if(tokenProvider.validateToken(accessToken)){
                Authentication authentication = tokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri : {}",authentication.getName(),requestURI);
            }else if(StringUtils.hasText(refreshToken)){
                boolean isRefreshToken = tokenProvider.refreshTokenValidation(refreshToken);
                if(isRefreshToken){
                    Member member = tokenProvider.getUserIdFromToken(refreshToken);

                    Authentication authentication =tokenProvider.getAuthenticationByUsername(member.getId());

                    String newAccessToken =  tokenProvider.createAccessToken(authentication);

                    setHeaderAccessToken((HttpServletResponse) response,newAccessToken);

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                }
            }

        }else{
            logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }
        chain.doFilter(request,response);
    }
    // Request Header 에서 토큰 정보를 꺼내오기 위한 메소드
    private String resolveToken(HttpServletRequest request, String type){
        return request.getHeader(type);
    }
    // 어세스 토큰 헤더 설정
    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader(AUTHORIATION_HEADER, accessToken);
    }


}
