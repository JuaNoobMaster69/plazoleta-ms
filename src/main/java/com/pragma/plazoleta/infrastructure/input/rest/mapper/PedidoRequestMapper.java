/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.plazoleta.infrastructure.input.rest.mapper;

import com.pragma.plazoleta.domain.model.DetallePedido;
import com.pragma.plazoleta.domain.model.Pedido;
import com.pragma.plazoleta.infrastructure.input.rest.dto.DetallePedidoResponseDto;
import com.pragma.plazoleta.infrastructure.input.rest.dto.PedidoRequestDto;
import com.pragma.plazoleta.infrastructure.input.rest.dto.PedidoResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Juan Camilo
 */
@Component
public class PedidoRequestMapper {

    public Pedido toModel(PedidoRequestDto dto) {
        Pedido p = new Pedido();
        p.setIdRestaurante(dto.idRestaurante());

        if (dto.platos() != null) {
            List<DetallePedido> detalles = dto.platos().stream().map(d -> {
                DetallePedido detalle = new DetallePedido();
                detalle.setIdPlato(d.idPlato());
                detalle.setCantidad(d.cantidad());
                return detalle;
            }).collect(Collectors.toList());
            p.setPlatos(detalles);
        }
        return p;
    }

        public PedidoResponseDto toResponse(Pedido p) {
        List<DetallePedidoResponseDto> platos = p.getPlatos() != null
                ? p.getPlatos().stream()
                    .map(d -> new DetallePedidoResponseDto(d.getIdPlato(), d.getCantidad()))
                    .collect(Collectors.toList())
                : null;

            return new PedidoResponseDto(
                    p.getId(),
                    p.getIdCliente(),
                    p.getIdRestaurante(),
                    p.getEstado() != null ? p.getEstado().name() : null,
                    p.getFechaCreacion(),
                    p.getIdEmpleado(),
                    p.getPinSeguridad(),
                    platos
            );
    }
}
