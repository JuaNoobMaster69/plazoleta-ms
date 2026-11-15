/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.plazoleta.infrastructure.input.rest.dto;
import java.util.List;
/**
 *
 * @author Juan Camilo
 */
public record PedidoRequestDto(
        Long idRestaurante,
        List<DetallePedidoRequestDto> platos) {
}