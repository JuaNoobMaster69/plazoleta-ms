/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.plazoleta.infrastructure.output.rest;

import com.pragma.plazoleta.domain.model.Pedido;
import com.pragma.plazoleta.domain.spi.ITrazabilidadClientPort;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

/**
 *
 * @author Juan Camilo
 */

@Component
public class TrazabilidadRestClientAdapter implements ITrazabilidadClientPort {

    private final RestClient restClient;

    public TrazabilidadRestClientAdapter() {
        this.restClient = RestClient.create("http://localhost:8085");
    }

    @Override
    public void registrarTraza(Pedido pedido, String estadoAnterior, String estadoNuevo) {
        Map<String, Object> body = Map.of(
                "idPedido", pedido.getId(),
                "idCliente", pedido.getIdCliente(),
                "idRestaurante", pedido.getIdRestaurante(),
                "estadoAnterior", estadoAnterior != null ? estadoAnterior : "N/A",
                "estadoNuevo", estadoNuevo,
                "idEmpleado", pedido.getIdEmpleado() != null ? pedido.getIdEmpleado() : 0
        );

        try {
            restClient.post()
                    .uri("/trazabilidad")
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();
            System.out.println(">>> Traza registrada en trazabilidad-ms");
        } catch (Exception e) {
            System.out.println(">>> Error al registrar traza: " + e.getMessage());
        }
    }
}
