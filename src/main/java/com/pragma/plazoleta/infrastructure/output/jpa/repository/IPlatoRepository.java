/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pragma.plazoleta.infrastructure.output.jpa.repository;

import com.pragma.plazoleta.domain.model.CategoriaPlato;
import com.pragma.plazoleta.infrastructure.output.jpa.entity.PlatoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPlatoRepository extends JpaRepository<PlatoEntity, Long> {
    
    // Para buscar todos los platos de un restaurante
    Page<PlatoEntity> findByIdRestaurante(Long idRestaurante, Pageable pageable);
    
    // Para buscar los platos de un restaurante filtrados por categoría
    Page<PlatoEntity> findByIdRestauranteAndCategoria(Long idRestaurante, CategoriaPlato categoria, Pageable pageable);
}