/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.plazoleta.infrastructure.output.jpa.mapper;

import com.pragma.plazoleta.domain.model.Restaurante;
import com.pragma.plazoleta.infrastructure.output.jpa.entity.RestauranteEntity;
import org.springframework.stereotype.Component;
/**
 *
 * @author Juan Camilo
 */
@Component
public class RestauranteEntityMapper {

    public RestauranteEntity toEntity(Restaurante r) {
        RestauranteEntity e = new RestauranteEntity();
        e.setId(r.getId());
        e.setNombre(r.getNombre());
        e.setNit(r.getNit());
        e.setDireccion(r.getDireccion());
        e.setTelefono(r.getTelefono());
        e.setUrlLogo(r.getUrlLogo());
        e.setIdPropietario(r.getIdPropietario());
        return e;
    }

    public Restaurante toModel(RestauranteEntity e) {
        Restaurante r = new Restaurante();
        r.setId(e.getId());
        r.setNombre(e.getNombre());
        r.setNit(e.getNit());
        r.setDireccion(e.getDireccion());
        r.setTelefono(e.getTelefono());
        r.setUrlLogo(e.getUrlLogo());
        r.setIdPropietario(e.getIdPropietario());
        return r;
    }
}