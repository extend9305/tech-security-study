package com.dong.tech.dto;

import com.dong.tech.domain.Member;
import lombok.*;

/**
 * The Class MemberDTO.
 *
 * @author dongsulee
 * @date 2023/09/22
 */
@Setter
@Getter
@NoArgsConstructor
public class MemberDTO {
    private Long seq;
    private String id;
    private String pwd;
    private String name;
    private String email;
    public Member toEntity(){
        Member build = Member.builder()
                .name(name)
                .email(email)
                .build();
        return build;
    }
    @Builder
    public MemberDTO(String id,String pwd, String name, String email) {
        this.id = id;
        this.pwd = pwd;
        this.name = name;
        this.email = email;
    }
}
