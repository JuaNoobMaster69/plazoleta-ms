/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.plazoleta.infrastructure.output.jpa.adapter;

import com.pragma.plazoleta.domain.model.Restaurante;
import com.pragma.plazoleta.domain.spi.IRestaurantePersistencePort;
import com.pragma.plazoleta.infrastructure.output.jpa.mapper.RestauranteEntityMapper;
import com.pragma.plazoleta.infrastructure.output.jpa.repository.IRestauranteRepository;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 *
 * @author Juan Camilo
 */
@Component
public class RestauranteJpaAdapter implements IRestaurantePersistencePort {

    private final IRestauranteRepository repository;
    private final RestauranteEntityMapper mapper;

    public RestauranteJpaAdapter(IRestauranteRepository repository, RestauranteEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Restaurante guardar(Restaurante restaurante) {
        return mapper.toModel(repository.save(mapper.toEntity(restaurante)));
    }

    @Override
    public boolean existePorNit(String nit) {
        return repository.existsByNit(nit);
    }

    @Override
    public boolean existePorId(Long id) {
        return repository.existsById(id);
    }

    @Override
    public Optional<Restaurante> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toModel);
    }
}