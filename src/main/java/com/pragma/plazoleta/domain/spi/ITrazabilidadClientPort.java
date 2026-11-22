/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.plazoleta.domain.spi;


import com.pragma.plazoleta.domain.model.Pedido;
/**
 *
 * @author Juan Camilo
 */
public interface ITrazabilidadClientPort {
    void registrarTraza(Pedido pedido, String estadoAnterior, String estadoNuevo);
}