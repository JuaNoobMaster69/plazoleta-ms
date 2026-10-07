/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.plazoleta.domain.exception;

/**
 *
 * @author Juan Camilo
 */
public class DomainException extends RuntimeException {

    public DomainException(String mensaje) {
        super(mensaje);
    }
}