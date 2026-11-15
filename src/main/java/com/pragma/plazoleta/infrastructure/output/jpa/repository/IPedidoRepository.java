/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pragma.plazoleta.infrastructure.output.jpa.repository;

import com.pragma.plazoleta.domain.model.EstadoPedido;
import com.pragma.plazoleta.infrastructure.output.jpa.entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 *
 * @author Juan Camilo
 */
public interface IPedidoRepository extends JpaRepository<PedidoEntity, Long> {

    boolean existsByIdClienteAndEstadoIn(Long idCliente, List<EstadoPedido> estados);
}
