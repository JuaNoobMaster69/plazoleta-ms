/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.plazoleta.infrastructure.input.rest;

import com.pragma.plazoleta.domain.api.IPedidoServicePort;
import com.pragma.plazoleta.infrastructure.input.rest.dto.PedidoRequestDto;
import com.pragma.plazoleta.infrastructure.input.rest.dto.PedidoResponseDto;
import com.pragma.plazoleta.infrastructure.input.rest.mapper.PedidoRequestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Juan Camilo
 */
@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final IPedidoServicePort servicePort;
    private final PedidoRequestMapper mapper;

    public PedidoController(IPedidoServicePort servicePort, PedidoRequestMapper mapper) {
        this.servicePort = servicePort;
        this.mapper = mapper;
    }

    @Operation(summary = "Realizar un pedido (solo el cliente)")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<PedidoResponseDto> crearPedido(@RequestBody PedidoRequestDto request) {
        var creado = servicePort.crearPedido(mapper.toModel(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(creado));
    }
}