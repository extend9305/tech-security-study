package com.dong.tech.mapper;

import com.dong.tech.domain.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * The Class MemberMapper.
 *
 * @author dongsulee
 * @date 2023/09/22
 */
@Mapper
public interface MemberMapper {
    List<Member> findAll();
    Member findById(@Param("id") String id);
    Member findByName(@Param("username")String username);
    Long save(@Param("id")String id, @Param("pwd")String pwd,@Param("name")String name);
    void saveRefreshToken(@Param("seq")Long seq, @Param("refreshToken")String refreshToken);

    Member findByRefreshToken(@Param("refreshToken")String refreshToken);
}
