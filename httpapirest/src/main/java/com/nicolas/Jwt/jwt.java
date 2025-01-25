package com.nicolas.Jwt;

import java.util.Date;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class jwt {

    public static boolean ValidaToken(String token){
        propriets j = new propriets();
        try {
            Algorithm algorithm = Algorithm.HMAC256(j.getJWT_SECRET_WORD());
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("Conta")
                    .build();

            DecodedJWT decodedJWT = verifier.verify(token);
                decodedJWT.getSubject();
                decodedJWT.getClaim("role").asString();
                
            return true;
        } catch (JWTVerificationException exception) {
            System.out.println(exception);
            return false;
        }
    }

    public static String GeraToken(String cpf){
        propriets j = new propriets();
        
        String NovoToken = JWT.create()
        .withIssuer("Conta")
                .withSubject(cpf)
                .withClaim("role", "Cliente")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000))
                .sign(Algorithm.HMAC256(j.getJWT_SECRET_WORD()));
        
        return NovoToken;
    }
}
