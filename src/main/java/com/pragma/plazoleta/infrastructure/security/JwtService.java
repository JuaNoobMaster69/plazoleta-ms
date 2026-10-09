/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.plazoleta.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author Juan Camilo
 */
@Component
public class JwtService {

    private final SecretKey clave;
    private final long expiracion;

    public JwtService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.expiration}") long expiracion) {
        this.clave = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiracion = expiracion;
    }

    /**
     * Genera un token JWT para un usuario con su rol.
     */
    public String generarToken(Long idUsuario, String correo, String rol) {
        Date ahora = new Date();
        Date expira = new Date(ahora.getTime() + expiracion);

        return Jwts.builder()
                .subject(correo)
                .claim("idUsuario", idUsuario)
                .claim("rol", rol)
                .issuedAt(ahora)
                .expiration(expira)
                .signWith(clave)
                .compact();
    }

    /**
     * Valida un token y extrae su contenido.
     * Si el token es inválido o expiró, lanza excepción.
     */
    public Claims validarToken(String token) {
        return Jwts.parser()
                .verifyWith(clave)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Extrae el correo (subject) del token.
     */
    public String obtenerCorreo(String token) {
        return validarToken(token).getSubject();
    }

    /**
     * Extrae el rol del token.
     */
    public String obtenerRol(String token) {
        return validarToken(token).get("rol", String.class);
    }

    /**
     * Extrae el id del usuario del token.
     */
    public Long obtenerIdUsuario(String token) {
        return validarToken(token).get("idUsuario", Long.class);
    }
}