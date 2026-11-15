/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pragma.plazoleta.domain.api;

import com.pragma.plazoleta.domain.model.Pagina;
import com.pragma.plazoleta.domain.model.Restaurante;
import java.util.List;

/**
 *
 * @author Juan Camilo
 */
public interface IRestauranteServicePort {

    Restaurante crearRestaurante(Restaurante restaurante);

    // Ahora solo recibe 'page' y devuelve Pagina
    Pagina<Restaurante> listarRestaurantes(int pagina, int tamano);
}