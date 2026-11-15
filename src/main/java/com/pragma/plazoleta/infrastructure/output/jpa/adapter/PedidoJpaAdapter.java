/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.plazoleta.infrastructure.output.jpa.adapter;

import com.pragma.plazoleta.domain.model.EstadoPedido;
import com.pragma.plazoleta.domain.model.Pedido;
import com.pragma.plazoleta.domain.spi.IPedidoPersistencePort;
import com.pragma.plazoleta.infrastructure.output.jpa.mapper.PedidoEntityMapper;
import com.pragma.plazoleta.infrastructure.output.jpa.repository.IPedidoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

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
}
