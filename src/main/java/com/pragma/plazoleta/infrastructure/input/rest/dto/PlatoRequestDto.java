/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.plazoleta.infrastructure.input.rest.dto;

import com.pragma.plazoleta.domain.model.CategoriaPlato;

/**
 *
 * @author Juan Camilo
 */
public record PlatoRequestDto(
        String nombre,
        Long precio,
        String descripcion,
        String urlImagen,
        CategoriaPlato categoria,
        Long idRestaurante) {
}