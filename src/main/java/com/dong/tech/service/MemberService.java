package com.dong.tech.service;

import com.dong.tech.domain.Member;
import com.dong.tech.mapper.MemberMapper;
import com.dong.tech.repository.MemberRepository;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The Class MemberService.
 *
 * @author dongsulee
 * @date 2023/09/22
 */
@Service
public class MemberService {
    private MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    public List<Member> getAllMember(){
        return memberRepository.findAll();
    }

    public Member findMember(String userId){
        return memberRepository.findById(userId);
    }
    public Member findByName(String username){
        return memberRepository.findByName(username);
    }

    public Long save(String userId, String pwd,String name){ return memberRepository.save(userId,pwd,name);}

    public void saveRefreshToken(Long seq, String refreshToken){ memberRepository.saveRefreshToken(seq,refreshToken);}

    public Member findByRefreshToken(String refreshToken){return memberRepository.findByRefreshToken(refreshToken);}
}
