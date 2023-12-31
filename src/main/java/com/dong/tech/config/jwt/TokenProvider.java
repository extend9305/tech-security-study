package com.dong.tech.config.jwt;

import com.dong.tech.domain.Member;
import com.dong.tech.service.MemberService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.el.parser.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The Class TokenProvider.
 *
 * @author dongsulee
 * @date 2023/10/05
 */
@Component
public class TokenProvider implements InitializingBean {
    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private static final String AUTHORITIES_KEY = "auth";
    private final String secret;
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;
    private Key key;
    private final UserDetailsService userDetailsService;
    private final MemberService memberService;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public TokenProvider(@Value("${jwt.secret}") String secret,@Value("${jwt.access-token-validity-in-seconds}") long accessTokenValidityInMilliseconds,@Value("${jwt.refresh-token-validity-in-seconds}") long refreshTokenValidityInMilliseconds,UserDetailsService userDetailsService,MemberService memberService,AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.secret = secret;
        this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds;
        this.userDetailsService = userDetailsService;
        this.memberService = memberService;
        this.authenticationManagerBuilder =authenticationManagerBuilder;
    }

    // 빈이 생성되고 주입을 받은 후에 secret값을 Base64 Decode해서 key 변수에 할당하기 위해
    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(Authentication authentication){

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        //access Token
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY,authorities) // 정보 저장
                .signWith(key, SignatureAlgorithm.HS512) // 사용할 암호화 알고리즘 , signature 에 들어갈 secret 값 생성.
                .setExpiration(new Date(now + this .accessTokenValidityInMilliseconds)) // expire time 셋팅.
                .compact();

        return accessToken;
    }
    public String createRefreshToken(Authentication authentication){
        //refresh Token
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        String refreshToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY,authorities) // 정보 저장
                .signWith(key, SignatureAlgorithm.HS512) // 사용할 암호화 알고리즘 , signature 에 들어갈 secret 값 생성.
                .setExpiration(new Date(now + this .refreshTokenValidityInMilliseconds * 10)) // expire time 셋팅.
                .compact();

        return refreshToken;
    }


    // 토큰으로 클레임을 만들고 이를 이용해 유저 객체를 만들어서 최종적으로 authentication 객체를 리턴
    public Authentication getAuthentication(String token){

        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // 디비를 거치지 않고 토큰에서 값을 꺼내 바로 시큐리티 유저 객체를 만들어 Authentication을 만들어 반환하기에 유저네임, 권한 외 정보는 알 수 없다.
        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public Authentication getAuthenticationByUsername(String userId) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰의 유효성 검증을 수행
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {

            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {

            logger.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {

            logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {

            logger.info("JWT 토큰이 잘못되었습니다.");
        } catch (Exception e){
            logger.debug(e.getMessage());
        }
        return false;
    }

    public Boolean refreshTokenValidation(String token){
        if(!validateToken(token)) return false;

        Long seq = memberService.findSeqByRefreshToken(token);
        if(seq == null){
            return false;
        }
        return true;
    }

    public Member getUserIdFromToken(String refreshToken){
        return memberService.findByRefreshToken(refreshToken);
    }
    public Authentication getAuthenticationToken(Authentication authenticationToken){
        return authenticationManagerBuilder.getObject().authenticate(authenticationToken);
    }



}
