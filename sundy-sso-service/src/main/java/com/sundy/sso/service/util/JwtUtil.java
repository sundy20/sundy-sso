package com.sundy.sso.service.util;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * Created on 2017/11/17
 *
 * @author plus.wang
 * @description
 */

public class JwtUtil {

    public static String generateToken(String secret, String subject) {

        JwtBuilder builder = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, secret);

        return builder.compact();
    }
}