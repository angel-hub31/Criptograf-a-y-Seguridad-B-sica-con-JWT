package com.krakedev.jwt.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import java.util.Date;

public class JwtUtil {

    // Firma secreta para el algoritmo HMAC256
    private static final String SECRET = "ClaveSecretaSuperSeguraPatitasAlRescate2026";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);
    
    // 30 minutos en milisegundos
    private static final long EXPIRATION_TIME = 30 * 60 * 1000; 

    // Generamos Token conteniendo username y rol como Claims
    public static String generarToken(String username, String rol) {
        return JWT.create()
                .withSubject(username)
                .withClaim("rol", rol)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(ALGORITHM);
    }

    // Validamos y Desencriptamos el Token JWT
    public static DecodedJWT verificarToken(String token) {
        JWTVerifier verifier = JWT.require(ALGORITHM).build();
        return verifier.verify(token);
    }
}