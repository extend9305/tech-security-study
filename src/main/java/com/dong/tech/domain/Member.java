package com.dong.tech.domain;

import com.dong.tech.mapper.MemberMapper;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.Serializable;

/**
 * The Class Member.
 *
 * @author dongsulee
 * @date 2023/09/22
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member implements Serializable {
    private Long seq;
    private String id;
    private String pwd;
    private String name;
    private String email;

    @Builder
    public Member(Long seq, String id, String pwd, String name, String email){
        this.seq = seq;
        this.id = id;
        this.pwd = pwd;
        this.name = name;
        this.email = email;
    }

    public static Member createUser(String userId, String pwd, PasswordEncoder passwordEncoder){
        return new Member(null,userId,passwordEncoder.encode(pwd),null,null);
    }

}
