/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Juan Camilo
 */
public class PedidoUseCase implements IPedidoServicePort {

    private final IPedidoPersistencePort pedidoPersistencePort;
    private final IRestaurantePersistencePort restaurantePersistencePort;
    private final IPlatoPersistencePort platoPersistencePort;
    private final IAuthenticatedUserPort authenticatedUserPort;
    private final IMensajeriaClientPort mensajeriaClientPort;

    public PedidoUseCase(IPedidoPersistencePort pedidoPersistencePort,
            IRestaurantePersistencePort restaurantePersistencePort,
            IPlatoPersistencePort platoPersistencePort,
            IAuthenticatedUserPort authenticatedUserPort,
            IMensajeriaClientPort mensajeriaClientPort) {
        this.pedidoPersistencePort = pedidoPersistencePort;
        this.restaurantePersistencePort = restaurantePersistencePort;
        this.platoPersistencePort = platoPersistencePort;
        this.authenticatedUserPort = authenticatedUserPort;
        this.mensajeriaClientPort = mensajeriaClientPort;
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
        // 1. Obtener el ID del empleado autenticado
        Long idEmpleado = authenticatedUserPort.obtenerIdUsuario();

        // 2. Buscar el pedido
        Pedido pedido = pedidoPersistencePort.buscarPorId(idPedido)
                .orElseThrow(() -> new DomainException("No existe un pedido con ese id"));

        // 3. Validar que el pedido esté en estado PENDIENTE
        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new DomainException("Solo se pueden asignar pedidos en estado PENDIENTE");
        }

        // 4. Asignar el empleado y cambiar el estado
        pedido.setIdEmpleado(idEmpleado);
        pedido.setEstado(EstadoPedido.EN_PREPARACION);

        return pedidoPersistencePort.guardar(pedido);
    }

    @Override
    public Pedido marcarPedidoListo(Long idPedido) {
        Pedido pedido = pedidoPersistencePort.buscarPorId(idPedido)
                .orElseThrow(() -> new DomainException("No existe un pedido con ese id"));

        if (pedido.getEstado() != EstadoPedido.EN_PREPARACION) {
            throw new DomainException("Solo se pueden marcar como LISTO los pedidos en EN_PREPARACION");
        }

        int pinInt = (int) (Math.random() * 900000) + 100000;
        pedido.setPinSeguridad(String.valueOf(pinInt));
        pedido.setEstado(EstadoPedido.LISTO);

        Pedido guardado = pedidoPersistencePort.guardar(pedido);

        // Llamada al microservicio de mensajería (simulada por ahora)
        mensajeriaClientPort.enviarSms("+573052763858", pedido.getPinSeguridad());

        return guardado;
    }

    @Override
    public Pedido entregarPedido(Long idPedido, String pin) {
        // 1. Buscar el pedido
        Pedido pedido = pedidoPersistencePort.buscarPorId(idPedido)
                .orElseThrow(() -> new DomainException("No existe un pedido con ese id"));

        // 2. Validar que esté en estado LISTO
        if (pedido.getEstado() != EstadoPedido.LISTO) {
            throw new DomainException("Solo se pueden entregar pedidos en estado LISTO");
        }

        // 3. Validar que el PIN coincida
        if (pin == null || !pin.equals(pedido.getPinSeguridad())) {
            throw new DomainException("El PIN de seguridad es incorrecto");
        }

        // 4. Cambiar el estado y guardar
        pedido.setEstado(EstadoPedido.ENTREGADO);
        return pedidoPersistencePort.guardar(pedido);
    }
    
    
}
