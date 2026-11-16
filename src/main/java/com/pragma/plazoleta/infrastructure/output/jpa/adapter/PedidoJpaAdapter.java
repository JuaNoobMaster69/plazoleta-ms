/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.plazoleta.infrastructure.output.jpa.adapter;

import com.pragma.plazoleta.domain.model.EstadoPedido;
import com.pragma.plazoleta.domain.model.Pagina;
import com.pragma.plazoleta.domain.model.Pedido;
import com.pragma.plazoleta.domain.spi.IPedidoPersistencePort;
import com.pragma.plazoleta.infrastructure.output.jpa.entity.PedidoEntity;
import com.pragma.plazoleta.infrastructure.output.jpa.mapper.PedidoEntityMapper;
import com.pragma.plazoleta.infrastructure.output.jpa.repository.IPedidoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Juan Camilo
 */
@Component
public class PedidoJpaAdapter implements IPedidoPersistencePort {

    private final IPedidoRepository repository;
    private final PedidoEntityMapper mapper;

    public PedidoJpaAdapter(IPedidoRepository repository, PedidoEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Pedido guardar(Pedido pedido) {
        return mapper.toModel(repository.save(mapper.toEntity(pedido)));
    }

    @Override
    public boolean existePedidoActivoPorCliente(Long idCliente, List<EstadoPedido> estados) {
        return repository.existsByIdClienteAndEstadoIn(idCliente, estados);
    }

    @Override
    public Pagina<Pedido> listarPorRestauranteYEstado(Long idRestaurante, EstadoPedido estado, int pagina, int tamano) {
        Pageable pageable = PageRequest.of(pagina, tamano);
        Page<PedidoEntity> pageEntity = repository.findByIdRestauranteAndEstado(idRestaurante, estado, pageable);

        List<Pedido> pedidos = pageEntity.getContent().stream()
                .map(mapper::toModel)
                .toList();

        return new Pagina<>(
                pedidos,
                pageEntity.getNumber(),
                pageEntity.getSize(),
                pageEntity.getTotalElements()
        );
    }

    @Override
    public Optional<Pedido> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toModel);
    }
}
