/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.plazoleta.infrastructure.output.rest;

import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.spi.IUsuarioClientPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
/**
 *
 * @author Juan Camilo
 */
@Component
public class UsuarioRestClientAdapter implements IUsuarioClientPort {

    private final RestClient restClient;

    public UsuarioRestClientAdapter(@Value("${usuarios.service.url}") String baseUrl) {
        this.restClient = RestClient.create(baseUrl);
    }

    @Override
    public String obtenerRol(Long idUsuario) {
        try {
            String rol = restClient.get()
                    .uri("/api/usuarios/{id}/rol", idUsuario)
                    .retrieve()
                    .body(String.class);
            return rol == null ? null : rol.replace("\"", "").trim();
        } catch (RestClientResponseException e) {
            throw new DomainException("No se encontró el usuario propietario indicado");
        } catch (ResourceAccessException e) {
            throw new DomainException("No fue posible validar el propietario: el servicio de usuarios no está disponible");
        }
    }
}