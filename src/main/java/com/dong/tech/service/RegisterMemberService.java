package com.dong.tech.service;

import com.dong.tech.domain.Member;
import com.dong.tech.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * The Class RegisterMemberService.
 *
 * @author dongsulee
 * @date 2023/10/04
 */
@Service
public class RegisterMemberService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository repository;

    @Autowired
    public RegisterMemberService(PasswordEncoder passwordEncoder, MemberRepository repository) {
        this.passwordEncoder = passwordEncoder;
        this.repository = repository;
    }

    public Long join(String userId, String pwd,String name){
        Member member = Member.createUser(userId,pwd,passwordEncoder);
        repository.save(member.getId(), member.getPwd(),name);

        return member.getSeq();
    }
}
