/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pragma.plazoleta.domain.api;

import com.pragma.plazoleta.domain.model.Plato;

/**
 *
 * @author Juan Camilo
 */
public interface IPlatoServicePort {

    Plato crearPlato(Plato plato);
    
    Plato modificarPlato(Long id, Long precio, String descripcion);
}