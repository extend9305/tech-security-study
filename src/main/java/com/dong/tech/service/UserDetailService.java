package com.dong.tech.service;

import com.dong.tech.domain.Member;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * The Class UserDetailService.
 *
 * @author dongsulee
 * @date 2023/09/25
 */
@Service
public class UserDetailService implements UserDetailsService {
    private final MemberService memberService;

    public UserDetailService(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Member member = memberService.findMember(id);
        Optional.ofNullable(member)
                .orElseThrow(()->new UsernameNotFoundException("회원을 찾을 수 없습니다."));

        return User.builder()
                .username(member.getName())
                .password(member.getPwd())
                .roles("User")
                .build();
    }
}
