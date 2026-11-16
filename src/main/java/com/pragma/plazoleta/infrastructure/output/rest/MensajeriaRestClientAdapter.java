/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.plazoleta.infrastructure.output.rest;

import com.pragma.plazoleta.domain.spi.IMensajeriaClientPort;
import org.springframework.stereotype.Component;
/**
 *
 * @author Juan Camilo
 */
@Component
public class MensajeriaRestClientAdapter implements IMensajeriaClientPort {

    @Override
    public void enviarSms(String celular, String pin) {
        // Por ahora solo lo imprimimos en consola.
        // En la Parte 2 lo conectaremos al microservicio real.
        System.out.println(">>> SIMULANDO ENVÍO DE SMS AL " + celular + " CON PIN: " + pin);
    }
}