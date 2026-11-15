/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pragma.plazoleta.domain.api;

/**
 *
 * @author Juan Camilo
 */
import com.pragma.plazoleta.domain.model.Pedido;

public interface IPedidoServicePort {

    Pedido crearPedido(Pedido pedido);
}
