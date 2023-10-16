package com.dong.tech.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.sql.Ref;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * The Class RefreshTokenRepository.
 *
 * @author dongsulee
 * @date 2023/10/16
 */
@Repository
public class RefreshTokenRepository {
    private RedisTemplate redisTemplate;

    public RefreshTokenRepository(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(Long seq , String refreshToken){
        ValueOperations<String,Long> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(refreshToken,seq);
        redisTemplate.expire(refreshToken,600L, TimeUnit.SECONDS);
    }

    public Long findBySeq(final String refreshToken){
        ValueOperations<String,Long> valueOperations = redisTemplate.opsForValue();
        Long seq = valueOperations.get(refreshToken);
        return seq;
    }
}
