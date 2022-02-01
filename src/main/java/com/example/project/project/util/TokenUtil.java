package com.example.project.project.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import static com.example.project.project.security.SecurityParameter.SECRET_KEY;

public class TokenUtil {

    public static DecodedJWT getDecodedJWT(final String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();

        return jwtVerifier.verify(token);
    }
}
