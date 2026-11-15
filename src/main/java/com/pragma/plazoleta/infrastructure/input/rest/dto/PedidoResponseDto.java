/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.plazoleta.infrastructure.input.rest.dto;

import java.time.LocalDateTime;
import java.util.List;
/**
 *
 * @author Juan Camilo
 */
public record PedidoResponseDto(
        Long id,
        Long idCliente,
        Long idRestaurante,
        String estado,
        LocalDateTime fechaCreacion,
        List<DetallePedidoResponseDto> platos) {
}