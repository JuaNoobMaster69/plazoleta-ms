/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.plazoleta.infrastructure.input.rest.mapper;

import com.pragma.plazoleta.domain.model.Plato;
import com.pragma.plazoleta.infrastructure.input.rest.dto.PlatoRequestDto;
import com.pragma.plazoleta.infrastructure.input.rest.dto.PlatoResponseDto;
import org.springframework.stereotype.Component;
/**
 *
 * @author Juan Camilo
 */
@Component
public class PlatoRequestMapper {

    public Plato toModel(PlatoRequestDto d) {
        Plato p = new Plato();
        p.setNombre(d.nombre());
        p.setPrecio(d.precio());
        p.setDescripcion(d.descripcion());
        p.setUrlImagen(d.urlImagen());
        p.setCategoria(d.categoria());
        p.setIdRestaurante(d.idRestaurante());
        return p;
    }

    public PlatoResponseDto toResponse(Plato p) {
        return new PlatoResponseDto(
                p.getId(),
                p.getNombre(),
                p.getPrecio(),
                p.getDescripcion(),
                p.getUrlImagen(),
                p.getCategoria(),
                p.getIdRestaurante(),
                p.getActivo());
    }
}