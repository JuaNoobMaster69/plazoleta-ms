/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.plazoleta.infrastructure.input.rest;

import com.pragma.plazoleta.domain.api.IPedidoServicePort;
import com.pragma.plazoleta.domain.model.EstadoPedido;
import com.pragma.plazoleta.domain.model.Pagina;
import com.pragma.plazoleta.domain.model.Pedido;
import com.pragma.plazoleta.infrastructure.input.rest.dto.PedidoRequestDto;
import com.pragma.plazoleta.infrastructure.input.rest.dto.PedidoResponseDto;
import com.pragma.plazoleta.infrastructure.input.rest.mapper.PedidoRequestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Operation(summary = "Listar pedidos por estado y restaurante (solo empleado)")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<Pagina<PedidoResponseDto>> listarPedidos(
            @RequestParam Long idRestaurante,
            @RequestParam EstadoPedido estado,
            @RequestParam(defaultValue = "0", name = "pagina") int pagina,
            @RequestParam(defaultValue = "10", name = "tamano") int tamano) {

        Pagina<Pedido> paginacion = servicePort.listarPedidos(idRestaurante, estado, pagina, tamano);
        Pagina<PedidoResponseDto> respuesta = paginacion.map(mapper::toResponse);

        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Asignarse a un pedido (solo empleado)")
    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/{id}/asignar")
    public ResponseEntity<PedidoResponseDto> asignarPedido(@PathVariable Long id) {
        var asignado = servicePort.asignarPedido(id);
        return ResponseEntity.ok(mapper.toResponse(asignado));
    }

    @Operation(summary = "Marcar pedido como LISTO y generar PIN (solo empleado)")
    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/{id}/listo")
    public ResponseEntity<PedidoResponseDto> marcarListo(@PathVariable Long id) {
        var listo = servicePort.marcarPedidoListo(id);
        return ResponseEntity.ok(mapper.toResponse(listo));
    }

    @Operation(summary = "Entregar un pedido (solo empleado)")
    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/{id}/entregar")
    public ResponseEntity<PedidoResponseDto> entregarPedido(
            @PathVariable Long id,
            @RequestParam String pin) {

        var entregado = servicePort.entregarPedido(id, pin);
        return ResponseEntity.ok(mapper.toResponse(entregado));
    }

}
