/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pragma.plazoleta.infrastructure.output.jpa.repository;


import com.pragma.plazoleta.infrastructure.output.jpa.entity.PlatoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 *
 * @author Juan Camilo
 */
public interface IPlatoRepository extends JpaRepository<PlatoEntity, Long> {
}