/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pragma.plazoleta.domain.spi;

import com.pragma.plazoleta.domain.model.Restaurante;

/**
 *
 * @author Juan Camilo
 */
public interface IRestaurantePersistencePort {

    Restaurante guardar(Restaurante restaurante);

    boolean existePorNit(String nit);
}