package com.pragma.plazoleta.application.usecase;

import com.pragma.plazoleta.domain.api.IPedidoServicePort;
import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.model.DetallePedido;
import com.pragma.plazoleta.domain.model.EstadoPedido;
import com.pragma.plazoleta.domain.model.Pagina;
import com.pragma.plazoleta.domain.model.Pedido;
import com.pragma.plazoleta.domain.spi.IAuthenticatedUserPort;
import com.pragma.plazoleta.domain.spi.IMensajeriaClientPort;
import com.pragma.plazoleta.domain.spi.IPedidoPersistencePort;
import com.pragma.plazoleta.domain.spi.IPlatoPersistencePort;
import com.pragma.plazoleta.domain.spi.IRestaurantePersistencePort;
import com.pragma.plazoleta.domain.spi.ITrazabilidadClientPort;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class PedidoUseCase implements IPedidoServicePort {

    private final IPedidoPersistencePort pedidoPersistencePort;
    private final IRestaurantePersistencePort restaurantePersistencePort;
    private final IPlatoPersistencePort platoPersistencePort;
    private final IAuthenticatedUserPort authenticatedUserPort;
    private final IMensajeriaClientPort mensajeriaClientPort;
    private final ITrazabilidadClientPort trazabilidadClientPort;

    public PedidoUseCase(IPedidoPersistencePort pedidoPersistencePort,
            IRestaurantePersistencePort restaurantePersistencePort,
            IPlatoPersistencePort platoPersistencePort,
            IAuthenticatedUserPort authenticatedUserPort,
            IMensajeriaClientPort mensajeriaClientPort,
            ITrazabilidadClientPort trazabilidadClientPort) {
        this.pedidoPersistencePort = pedidoPersistencePort;
        this.restaurantePersistencePort = restaurantePersistencePort;
        this.platoPersistencePort = platoPersistencePort;
        this.authenticatedUserPort = authenticatedUserPort;
        this.mensajeriaClientPort = mensajeriaClientPort;
        this.trazabilidadClientPort = trazabilidadClientPort;
    }

    @Override
    public Pedido crearPedido(Pedido pedido) {
        Long idCliente = authenticatedUserPort.obtenerIdUsuario();
        pedido.setIdCliente(idCliente);

        restaurantePersistencePort.buscarPorId(pedido.getIdRestaurante())
                .orElseThrow(() -> new DomainException("El restaurante indicado no existe"));

        List<EstadoPedido> estadosActivos = Arrays.asList(
                EstadoPedido.PENDIENTE,
                EstadoPedido.EN_PREPARACION,
                EstadoPedido.LISTO
        );
        if (pedidoPersistencePort.existePedidoActivoPorCliente(idCliente, estadosActivos)) {
            throw new DomainException("El cliente ya tiene un pedido en proceso");
        }

        if (pedido.getPlatos() == null || pedido.getPlatos().isEmpty()) {
            throw new DomainException("El pedido debe tener al menos un plato");
        }

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

        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setFechaCreacion(LocalDateTime.now());

        Pedido guardado = pedidoPersistencePort.guardar(pedido);
        trazabilidadClientPort.registrarTraza(guardado, "N/A", "PENDIENTE");
        return guardado;
    }

    @Override
    public Pagina<Pedido> listarPedidos(Long idRestaurante, EstadoPedido estado, int pagina, int tamano) {
        if (pagina < 0) {
            throw new DomainException("El número de página no puede ser negativo");
        }
        if (tamano < 1) {
            throw new DomainException("El tamaño de la página debe ser al menos 1");
        }
        if (idRestaurante == null) {
            throw new DomainException("El id del restaurante es obligatorio");
        }
        if (estado == null) {
            throw new DomainException("El estado del pedido es obligatorio");
        }
        return pedidoPersistencePort.listarPorRestauranteYEstado(idRestaurante, estado, pagina, tamano);
    }

    @Override
    public Pedido asignarPedido(Long idPedido) {
        Long idEmpleado = authenticatedUserPort.obtenerIdUsuario();
        Pedido pedido = pedidoPersistencePort.buscarPorId(idPedido)
                .orElseThrow(() -> new DomainException("No existe un pedido con ese id"));

        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new DomainException("Solo se pueden asignar pedidos en estado PENDIENTE");
        }

        String estadoAnterior = pedido.getEstado().name();
        pedido.setIdEmpleado(idEmpleado);
        pedido.setEstado(EstadoPedido.EN_PREPARACION);

        Pedido guardado = pedidoPersistencePort.guardar(pedido);
        trazabilidadClientPort.registrarTraza(guardado, estadoAnterior, "EN_PREPARACION");
        return guardado;
    }

    @Override
    public Pedido marcarPedidoListo(Long idPedido) {
        Pedido pedido = pedidoPersistencePort.buscarPorId(idPedido)
                .orElseThrow(() -> new DomainException("No existe un pedido con ese id"));

        if (pedido.getEstado() != EstadoPedido.EN_PREPARACION) {
            throw new DomainException("Solo se pueden marcar como LISTO los pedidos en EN_PREPARACION");
        }

        String estadoAnterior = pedido.getEstado().name();
        int pinInt = (int) (Math.random() * 900000) + 100000;
        pedido.setPinSeguridad(String.valueOf(pinInt));
        pedido.setEstado(EstadoPedido.LISTO);

        Pedido guardado = pedidoPersistencePort.guardar(pedido);
        
        // Notificar al cliente
        mensajeriaClientPort.enviarSms("+573052763858", pedido.getPinSeguridad());
        
        // Registrar traza
        trazabilidadClientPort.registrarTraza(guardado, estadoAnterior, "LISTO");
        
        return guardado;
    }

    @Override
    public Pedido entregarPedido(Long idPedido, String pin) {
        Pedido pedido = pedidoPersistencePort.buscarPorId(idPedido)
                .orElseThrow(() -> new DomainException("No existe un pedido con ese id"));

        if (pedido.getEstado() != EstadoPedido.LISTO) {
            throw new DomainException("Solo se pueden entregar pedidos en estado LISTO");
        }

        if (pin == null || !pin.equals(pedido.getPinSeguridad())) {
            throw new DomainException("El PIN de seguridad es incorrecto");
        }

        String estadoAnterior = pedido.getEstado().name();
        pedido.setEstado(EstadoPedido.ENTREGADO);

        Pedido guardado = pedidoPersistencePort.guardar(pedido);
        trazabilidadClientPort.registrarTraza(guardado, estadoAnterior, "ENTREGADO");
        return guardado;
    }

    @Override
    public Pedido cancelarPedido(Long idPedido) {
        Pedido pedido = pedidoPersistencePort.buscarPorId(idPedido)
                .orElseThrow(() -> new DomainException("No existe un pedido con ese id"));

        Long idCliente = authenticatedUserPort.obtenerIdUsuario();
        if (!pedido.getIdCliente().equals(idCliente)) {
            throw new DomainException("No tiene permiso para cancelar este pedido");
        }

        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new DomainException("Lo sentimos, tu pedido ya está en preparación y no puede cancelarse");
        }

        String estadoAnterior = pedido.getEstado().name();
        pedido.setEstado(EstadoPedido.CANCELADO);

        Pedido guardado = pedidoPersistencePort.guardar(pedido);
        trazabilidadClientPort.registrarTraza(guardado, estadoAnterior, "CANCELADO");
        return guardado;
    }
}