//package com.dong.tech.config.filter;
//
//import com.dong.tech.config.jwt.TokenProvider;
//import com.dong.tech.domain.Member;
//import com.dong.tech.dto.MemberDTO;
//import com.dong.tech.service.MemberService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.AllArgsConstructor;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.Optional;
//
///**
// * The Class UserAuthenticationFilter.
// *
// * @author dongsulee
// * @date 2023/10/05
// */
//@AllArgsConstructor
//public class UserAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//    private final TokenProvider tokenProvider;
//    private final MemberService memberService;
//    private final PasswordEncoder passwordEncoder;
//
//    private final AuthenticationManager authenticationManager;
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            MemberDTO memberDTO = objectMapper.readValue(request.getInputStream(), MemberDTO.class);
//            Member member = memberService.findMember(memberDTO.getId());
//            Optional.ofNullable(member)
//                    .orElseThrow(()->new UsernameNotFoundException("회원을 찾을 수 없습니다."));
//            if(!passwordEncoder.matches(memberDTO.getPwd(),member.getPwd())) {
//                throw new IllegalArgumentException("잘못된 비밀번호 입니다.");
//            }
//            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberDTO.getId(), memberDTO.getPwd());
//
//            return authenticationManager.authenticate(authenticationToken);
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
