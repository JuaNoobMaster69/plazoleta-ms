/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.plazoleta.infrastructure.input.rest;

import com.pragma.plazoleta.domain.api.IPlatoServicePort;
import com.pragma.plazoleta.infrastructure.input.rest.dto.PlatoRequestDto;
import com.pragma.plazoleta.infrastructure.input.rest.dto.PlatoResponseDto;
import com.pragma.plazoleta.infrastructure.input.rest.mapper.PlatoRequestMapper;
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
@RequestMapping("/platos")
public class PlatoController {

    private final IPlatoServicePort servicePort;
    private final PlatoRequestMapper mapper;

    public PlatoController(IPlatoServicePort servicePort, PlatoRequestMapper mapper) {
        this.servicePort = servicePort;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<PlatoResponseDto> crearPlato(@RequestBody PlatoRequestDto request) {
        var creado = servicePort.crearPlato(mapper.toModel(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(creado));
    }
}