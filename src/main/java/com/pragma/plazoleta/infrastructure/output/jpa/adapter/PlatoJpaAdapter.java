/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.plazoleta.infrastructure.output.jpa.adapter;

import com.pragma.plazoleta.domain.model.CategoriaPlato;
import com.pragma.plazoleta.domain.model.Pagina;
import com.pragma.plazoleta.domain.model.Plato;
import com.pragma.plazoleta.domain.spi.IPlatoPersistencePort;
import com.pragma.plazoleta.infrastructure.output.jpa.entity.PlatoEntity;
import com.pragma.plazoleta.infrastructure.output.jpa.mapper.PlatoEntityMapper;
import com.pragma.plazoleta.infrastructure.output.jpa.repository.IPlatoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Optional<Plato> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    public Pagina<Plato> listarPorRestauranteYCategoria(Long idRestaurante, CategoriaPlato categoria, int pagina, int tamano) {
        Pageable pageable = PageRequest.of(pagina, tamano);
        Page<PlatoEntity> pageEntity;

        if (categoria != null) {
            pageEntity = repository.findByIdRestauranteAndCategoria(idRestaurante, categoria, pageable);
        } else {
            pageEntity = repository.findByIdRestaurante(idRestaurante, pageable);
        }

        List<Plato> platos = pageEntity.getContent().stream()
                .map(mapper::toModel)
                .toList();

        return new Pagina<>(
                platos,
                pageEntity.getNumber(),
                pageEntity.getSize(),
                pageEntity.getTotalElements()
        );
    }
}