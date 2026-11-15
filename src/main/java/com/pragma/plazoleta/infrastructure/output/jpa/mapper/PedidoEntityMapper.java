/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.plazoleta.infrastructure.output.jpa.mapper;

import com.pragma.plazoleta.domain.model.DetallePedido;
import com.pragma.plazoleta.domain.model.Pedido;
import com.pragma.plazoleta.infrastructure.output.jpa.entity.DetallePedidoEntity;
import com.pragma.plazoleta.infrastructure.output.jpa.entity.PedidoEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
/**
 *
 * @author Juan Camilo
 */
@Component
public class PedidoEntityMapper {

    public PedidoEntity toEntity(Pedido p) {
        PedidoEntity e = new PedidoEntity();
        e.setId(p.getId());
        e.setIdCliente(p.getIdCliente());
        e.setIdRestaurante(p.getIdRestaurante());
        e.setEstado(p.getEstado());
        e.setFechaCreacion(p.getFechaCreacion());
        e.setIdEmpleado(p.getIdEmpleado());
        e.setPinSeguridad(p.getPinSeguridad());

        if (p.getPlatos() != null) {
            List<DetallePedidoEntity> detalles = p.getPlatos().stream().map(d -> {
                DetallePedidoEntity de = new DetallePedidoEntity();
                de.setIdPlato(d.getIdPlato());
                de.setCantidad(d.getCantidad());
                return de;
            }).collect(Collectors.toList());
            e.setPlatos(detalles);
        }
        return e;
    }

    public Pedido toModel(PedidoEntity e) {
        Pedido p = new Pedido();
        p.setId(e.getId());
        p.setIdCliente(e.getIdCliente());
        p.setIdRestaurante(e.getIdRestaurante());
        p.setEstado(e.getEstado());
        p.setFechaCreacion(e.getFechaCreacion());
        p.setIdEmpleado(e.getIdEmpleado());
        p.setPinSeguridad(e.getPinSeguridad());

        if (e.getPlatos() != null) {
            List<DetallePedido> detalles = e.getPlatos().stream().map(de -> {
                DetallePedido d = new DetallePedido();
                d.setIdPlato(de.getIdPlato());
                d.setCantidad(de.getCantidad());
                return d;
            }).collect(Collectors.toList());
            p.setPlatos(detalles);
        }
        return p;
    }
}