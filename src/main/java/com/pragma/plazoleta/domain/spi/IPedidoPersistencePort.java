/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pragma.plazoleta.domain.spi;

/**
 *
 * @author Juan Camilo
 */
import com.pragma.plazoleta.domain.model.EstadoPedido;
import com.pragma.plazoleta.domain.model.Pagina;
import com.pragma.plazoleta.domain.model.Pedido;
import java.util.List;
import java.util.Optional;

public interface IPedidoPersistencePort {

    Pedido guardar(Pedido pedido);

    // Para validar que el cliente no tenga pedidos en proceso
    boolean existePedidoActivoPorCliente(Long idCliente, List<EstadoPedido> estados);

    Pagina<Pedido> listarPorRestauranteYEstado(Long idRestaurante, EstadoPedido estado, int pagina, int tamano);

    Optional<Pedido> buscarPorId(Long id);
    
}
