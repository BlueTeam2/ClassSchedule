package com.softserve.security.jwt;

import com.softserve.entity.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().toString())),
                (user.getToken() == null || user.getToken().equals(""))
        );
    }
}
