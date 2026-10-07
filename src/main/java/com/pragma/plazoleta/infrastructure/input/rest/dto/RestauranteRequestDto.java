/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.plazoleta.infrastructure.input.rest.dto;

/**
 *
 * @author Juan Camilo
 */
public record RestauranteRequestDto(String nombre, String nit, String direccion,
        String telefono, String urlLogo, Long idPropietario) {

}
