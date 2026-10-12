/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.plazoleta.infrastructure.input.rest.mapper;

import com.pragma.plazoleta.domain.model.Restaurante;
import com.pragma.plazoleta.infrastructure.input.rest.dto.RestauranteListadoDto;
import com.pragma.plazoleta.infrastructure.input.rest.dto.RestauranteRequestDto;
import com.pragma.plazoleta.infrastructure.input.rest.dto.RestauranteResponseDto;
import org.springframework.stereotype.Component;

/**
 *
 * @author Juan Camilo
 */
@Component
public class RestauranteRequestMapper {

    public Restaurante toModel(RestauranteRequestDto d) {
        Restaurante r = new Restaurante();
        r.setNombre(d.nombre());
        r.setNit(d.nit());
        r.setDireccion(d.direccion());
        r.setTelefono(d.telefono());
        r.setUrlLogo(d.urlLogo());
        r.setIdPropietario(d.idPropietario());
        return r;
    }

    public RestauranteResponseDto toResponse(Restaurante r) {
        return new RestauranteResponseDto(r.getId(), r.getNombre(), r.getNit(),
                r.getDireccion(), r.getTelefono(), r.getUrlLogo(), r.getIdPropietario());
    }

    public RestauranteListadoDto toListadoDto(Restaurante r) {
        return new RestauranteListadoDto(r.getNombre(), r.getUrlLogo());
    }
}