/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pragma.plazoleta.infrastructure.output.jpa.repository;

import com.pragma.plazoleta.infrastructure.output.jpa.entity.RestauranteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Juan Camilo
 */
public interface IRestauranteRepository extends JpaRepository<RestauranteEntity, Long> {

    boolean existsByNit(String nit);

    Page<RestauranteEntity> findAllByOrderByNombreAsc(Pageable pageable);
}