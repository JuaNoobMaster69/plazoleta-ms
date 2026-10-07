/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.plazoleta.infrastructure.output.jpa.adapter;

import com.pragma.plazoleta.domain.model.Plato;
import com.pragma.plazoleta.domain.spi.IPlatoPersistencePort;
import com.pragma.plazoleta.infrastructure.output.jpa.mapper.PlatoEntityMapper;
import com.pragma.plazoleta.infrastructure.output.jpa.repository.IPlatoRepository;
import org.springframework.stereotype.Component;
/**
 *
 * @author Juan Camilo
 */
@Component
public class PlatoJpaAdapter implements IPlatoPersistencePort {

    private final IPlatoRepository repository;
    private final PlatoEntityMapper mapper;

    public PlatoJpaAdapter(IPlatoRepository repository, PlatoEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Plato guardar(Plato plato) {
        return mapper.toModel(repository.save(mapper.toEntity(plato)));
    }
}