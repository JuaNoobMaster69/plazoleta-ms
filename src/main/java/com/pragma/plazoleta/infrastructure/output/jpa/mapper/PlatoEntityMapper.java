/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.plazoleta.infrastructure.output.jpa.mapper;

import com.pragma.plazoleta.domain.model.Plato;
import com.pragma.plazoleta.infrastructure.output.jpa.entity.PlatoEntity;
import org.springframework.stereotype.Component;
/**
 *
 * @author Juan Camilo
 */
@Component
public class PlatoEntityMapper {

    public PlatoEntity toEntity(Plato p) {
        PlatoEntity e = new PlatoEntity();
        e.setId(p.getId());
        e.setNombre(p.getNombre());
        e.setPrecio(p.getPrecio());
        e.setDescripcion(p.getDescripcion());
        e.setUrlImagen(p.getUrlImagen());
        e.setCategoria(p.getCategoria());
        e.setIdRestaurante(p.getIdRestaurante());
        e.setActivo(p.getActivo());
        return e;
    }

    public Plato toModel(PlatoEntity e) {
        Plato p = new Plato();
        p.setId(e.getId());
        p.setNombre(e.getNombre());
        p.setPrecio(e.getPrecio());
        p.setDescripcion(e.getDescripcion());
        p.setUrlImagen(e.getUrlImagen());
        p.setCategoria(e.getCategoria());
        p.setIdRestaurante(e.getIdRestaurante());
        p.setActivo(e.getActivo());
        return p;
    }
}