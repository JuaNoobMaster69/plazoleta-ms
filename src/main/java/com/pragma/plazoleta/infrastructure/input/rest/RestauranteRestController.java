/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.plazoleta.infrastructure.input.rest;

import com.pragma.plazoleta.domain.api.IRestauranteServicePort;
import com.pragma.plazoleta.infrastructure.input.rest.dto.RestauranteListadoDto;
import com.pragma.plazoleta.infrastructure.input.rest.dto.RestauranteRequestDto;
import com.pragma.plazoleta.infrastructure.input.rest.dto.RestauranteResponseDto;
import com.pragma.plazoleta.infrastructure.input.rest.mapper.RestauranteRequestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/restaurantes")
public class RestauranteRestController {

    private final IRestauranteServicePort servicePort;
    private final RestauranteRequestMapper mapper;

    public RestauranteRestController(IRestauranteServicePort servicePort, RestauranteRequestMapper mapper) {
        this.servicePort = servicePort;
        this.mapper = mapper;
    }

    @Operation(summary = "Crear un restaurante (solo el administrador)")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<RestauranteResponseDto> crearRestaurante(@RequestBody RestauranteRequestDto request) {
        var creado = servicePort.crearRestaurante(mapper.toModel(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(creado));
    }

    @Operation(summary = "Listar restaurantes por orden alfabético (público)")
    @GetMapping
    public ResponseEntity<List<RestauranteListadoDto>> listarRestaurantes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var lista = servicePort.listarRestaurantes(page, size)
                .stream()
                .map(mapper::toListadoDto)
                .toList();
        return ResponseEntity.ok(lista);
    }
}