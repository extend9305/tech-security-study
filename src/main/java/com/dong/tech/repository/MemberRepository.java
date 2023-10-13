package com.dong.tech.repository;

import com.dong.tech.domain.Member;
import com.dong.tech.mapper.MemberMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The Class MemberRepository.
 *
 * @author dongsulee
 * @date 2023/09/22
 */
@Repository
public class MemberRepository {
    private MemberMapper mapper;

    @Autowired
    public MemberRepository(MemberMapper mapper) {
        this.mapper = mapper;
    }

    public List<Member> findAll() {
        return mapper.findAll();
    }

    public Member findById(String id) {
        return mapper.findById(id);
    }
    public Member findByName(String username){
        return mapper.findByName(username);
    }

    public Long save(String id, String pwd,String name){return mapper.save(id,pwd,name);}

    public void saveRefreshToken(Long seq , String refreshToken){ mapper.saveRefreshToken(seq,refreshToken);}

    public Member findByRefreshToken(@Param("refreshToken")String refreshToken){ return mapper.findByRefreshToken(refreshToken);}
}
