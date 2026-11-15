/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.plazoleta.infrastructure.input.rest;

import com.pragma.plazoleta.domain.api.IPlatoServicePort;
import com.pragma.plazoleta.domain.model.CategoriaPlato;
import com.pragma.plazoleta.domain.model.Pagina;
import com.pragma.plazoleta.domain.model.Plato;
import com.pragma.plazoleta.infrastructure.input.rest.dto.PlatoEstadoRequestDto;
import com.pragma.plazoleta.infrastructure.input.rest.dto.PlatoRequestDto;
import com.pragma.plazoleta.infrastructure.input.rest.dto.PlatoResponseDto;
import com.pragma.plazoleta.infrastructure.input.rest.dto.PlatoUpdateRequestDto;
import com.pragma.plazoleta.infrastructure.input.rest.mapper.PlatoRequestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Juan Camilo
 */
@RestController
@RequestMapping("/platos")
public class PlatoController {

    private final IPlatoServicePort servicePort;
    private final PlatoRequestMapper mapper;

    public PlatoController(IPlatoServicePort servicePort, PlatoRequestMapper mapper) {
        this.servicePort = servicePort;
        this.mapper = mapper;
    }

    @Operation(summary = "Crear un plato (solo el propietario del restaurante)")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<PlatoResponseDto> crearPlato(@RequestBody PlatoRequestDto request) {
        var creado = servicePort.crearPlato(mapper.toModel(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(creado));
    }

    @Operation(summary = "Modificar un plato (solo el propietario del restaurante)")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<PlatoResponseDto> modificarPlato(
            @PathVariable Long id,
            @RequestBody PlatoUpdateRequestDto request) {
        var modificado = servicePort.modificarPlato(id, request.precio(), request.descripcion());
        return ResponseEntity.ok(mapper.toResponse(modificado));
    }

    @Operation(summary = "Habilitar/Deshabilitar un plato (solo el propietario del restaurante)")
    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<PlatoResponseDto> cambiarEstado(
            @PathVariable Long id,
            @RequestBody PlatoEstadoRequestDto request) {
        var actualizado = servicePort.cambiarEstadoPlato(id, request.activo());
        return ResponseEntity.ok(mapper.toResponse(actualizado));
    }

    @Operation(summary = "Listar platos de un restaurante por categoría (público)")
    @GetMapping("/restaurante/{idRestaurante}")
    public ResponseEntity<Pagina<PlatoResponseDto>> listarPlatos(
            @PathVariable Long idRestaurante,
            @RequestParam(required = false) CategoriaPlato categoria,
            @RequestParam(defaultValue = "0", name = "pagina") int pagina,
            @RequestParam(defaultValue = "10", name = "tamano") int tamano) {

        Pagina<Plato> paginacion = servicePort.listarPlatos(idRestaurante, categoria, pagina, tamano);
        Pagina<PlatoResponseDto> respuesta = paginacion.map(mapper::toResponse);

        return ResponseEntity.ok(respuesta);
    }
}
