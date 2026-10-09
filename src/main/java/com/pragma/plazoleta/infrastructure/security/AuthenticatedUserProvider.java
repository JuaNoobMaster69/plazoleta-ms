/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.plazoleta.infrastructure.security;

import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.spi.IAuthenticatedUserPort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 *
 * @author Juan Camilo
 */
@Component
public class AuthenticatedUserProvider implements IAuthenticatedUserPort {

    @Override
    public Long obtenerIdUsuario() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getDetails() == null) {
            throw new DomainException("No hay un usuario autenticado en la petición");
        }
        return (Long) auth.getDetails();
    }
}