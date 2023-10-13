package com.dong.tech.controller;

import com.dong.tech.config.jwt.TokenProvider;
import com.dong.tech.domain.Member;
import com.dong.tech.dto.MemberDTO;
import com.dong.tech.service.MemberService;
import com.dong.tech.service.RegisterMemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sound.midi.MetaMessage;
import java.util.Optional;

/**
 * The Class AuthorizationController.
 *
 * @author dongsulee
 * @date 2023/10/04
 */
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthorizationController {
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RegisterMemberService registerMemberService;
    private final MemberService memberService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

//  이렇게 말고 spring security 에 filter 로 로그인 처리.

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberDTO memberDTO, HttpServletResponse response){
        Member member = memberService.findMember(memberDTO.getId());
        Optional.ofNullable(member)
                .orElseThrow(()->new UsernameNotFoundException("회원을 찾을 수 없습니다."));
        if(!passwordEncoder.matches(memberDTO.getPwd(),member.getPwd())) {
            throw new IllegalArgumentException("잘못된 비밀번호 입니다.");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberDTO.getId(), memberDTO.getPwd());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        String accessToken =  tokenProvider.createAccessToken(authentication);
        String refreshToken =  tokenProvider.createRefreshToken(authentication);

        memberService.saveRefreshToken(member.getSeq(),refreshToken);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization",accessToken);
        httpHeaders.add("RefreshToken",refreshToken);

        return new ResponseEntity<>(accessToken,httpHeaders, HttpStatus.OK);
    }


    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody MemberDTO memberDTO){
        try {
            registerMemberService.join(memberDTO.getId(),memberDTO.getPwd(),memberDTO.getName());
            return ResponseEntity.ok("join success");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
