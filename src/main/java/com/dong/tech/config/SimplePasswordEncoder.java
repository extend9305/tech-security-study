package com.dong.tech.config;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * The Class SimplePasswordEncoder.
 *
 * @author dongsulee
 * @date 2023/09/25
 */
public class SimplePasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(encode(rawPassword));
    }
}
