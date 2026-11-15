/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.plazoleta.application.usecase;

import com.pragma.plazoleta.domain.api.IPedidoServicePort;
import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.model.DetallePedido;
import com.pragma.plazoleta.domain.model.EstadoPedido;
import com.pragma.plazoleta.domain.model.Pedido;
import com.pragma.plazoleta.domain.spi.IAuthenticatedUserPort;
import com.pragma.plazoleta.domain.spi.IPedidoPersistencePort;
import com.pragma.plazoleta.domain.spi.IPlatoPersistencePort;
import com.pragma.plazoleta.domain.spi.IRestaurantePersistencePort;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Juan Camilo
 */
public class PedidoUseCase implements IPedidoServicePort {

    private final IPedidoPersistencePort pedidoPersistencePort;
    private final IRestaurantePersistencePort restaurantePersistencePort;
    private final IPlatoPersistencePort platoPersistencePort;
    private final IAuthenticatedUserPort authenticatedUserPort;

    public PedidoUseCase(IPedidoPersistencePort pedidoPersistencePort,
            IRestaurantePersistencePort restaurantePersistencePort,
            IPlatoPersistencePort platoPersistencePort,
            IAuthenticatedUserPort authenticatedUserPort) {
        this.pedidoPersistencePort = pedidoPersistencePort;
        this.restaurantePersistencePort = restaurantePersistencePort;
        this.platoPersistencePort = platoPersistencePort;
        this.authenticatedUserPort = authenticatedUserPort;
    }

    @Override
    public Pedido crearPedido(Pedido pedido) {
        // 1. Obtener el ID del cliente autenticado
        Long idCliente = authenticatedUserPort.obtenerIdUsuario();
        pedido.setIdCliente(idCliente);

        // 2. Validar que el restaurante exista
        restaurantePersistencePort.buscarPorId(pedido.getIdRestaurante())
                .orElseThrow(() -> new DomainException("El restaurante indicado no existe"));

        // 3. Validar que el cliente no tenga un pedido activo
        List<EstadoPedido> estadosActivos = Arrays.asList(
                EstadoPedido.PENDIENTE,
                EstadoPedido.EN_PREPARACION,
                EstadoPedido.LISTO
        );
        if (pedidoPersistencePort.existePedidoActivoPorCliente(idCliente, estadosActivos)) {
            throw new DomainException("El cliente ya tiene un pedido en proceso");
        }

        // 4. Validar que la lista de platos no esté vacía
        if (pedido.getPlatos() == null || pedido.getPlatos().isEmpty()) {
            throw new DomainException("El pedido debe tener al menos un plato");
        }

        // 5. Validar que los platos existan, estén activos y pertenezcan al restaurante
        for (DetallePedido detalle : pedido.getPlatos()) {
            if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                throw new DomainException("La cantidad de cada plato debe ser mayor a 0");
            }

            var platoOpt = platoPersistencePort.buscarPorId(detalle.getIdPlato());
            if (platoOpt.isEmpty()) {
                throw new DomainException("El plato con ID " + detalle.getIdPlato() + " no existe");
            }

            var plato = platoOpt.get();
            if (!plato.getActivo()) {
                throw new DomainException("El plato " + plato.getNombre() + " no está disponible");
            }
            if (!plato.getIdRestaurante().equals(pedido.getIdRestaurante())) {
                throw new DomainException("El plato " + plato.getNombre() + " no pertenece al restaurante seleccionado");
            }
        }

        // 6. Asignar estado y fecha por defecto
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setFechaCreacion(LocalDateTime.now());

        return pedidoPersistencePort.guardar(pedido);
    }
}
