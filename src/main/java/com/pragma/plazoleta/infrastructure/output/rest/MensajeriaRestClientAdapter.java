/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.plazoleta.infrastructure.output.rest;

import com.pragma.plazoleta.domain.spi.IMensajeriaClientPort;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.Map;

/**
 *
 * @author Juan Camilo
 */
@Component
public class MensajeriaRestClientAdapter implements IMensajeriaClientPort {

    private final RestClient restClient;

    public MensajeriaRestClientAdapter() {
        this.restClient = RestClient.create("http://localhost:8084");
    }

    @Override
    public void enviarSms(String celular, String pin) {
        Map<String, String> body = Map.of(
                "celular", celular,
                "pin", pin
        );
        try {
            restClient.post()
                    .uri("/sms/enviar")
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();
            System.out.println(">>> Notificación enviada al microservicio de mensajería");
        } catch (Exception e) {
            System.out.println(">>> Error al llamar al microservicio de mensajería: " + e.getMessage());
        }
    }
}