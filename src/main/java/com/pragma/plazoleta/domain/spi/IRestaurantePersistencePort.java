/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pragma.plazoleta.domain.spi;

import com.pragma.plazoleta.domain.model.Restaurante;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Juan Camilo
 */
public interface IRestaurantePersistencePort {

    Restaurante guardar(Restaurante restaurante);

    boolean existePorNit(String nit);

    boolean existePorId(Long id);

    Optional<Restaurante> buscarPorId(Long id);

    List<Restaurante> listarPaginado(int page, int size);
}