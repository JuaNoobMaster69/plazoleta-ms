/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.plazoleta.application.usecase;

import com.pragma.plazoleta.domain.api.IRestauranteServicePort;
import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.model.Restaurante;
import com.pragma.plazoleta.domain.spi.IRestaurantePersistencePort;
import com.pragma.plazoleta.domain.spi.IUsuarioClientPort;
/**
 *
 * @author Juan Camilo
 */
public class RestauranteUseCase implements IRestauranteServicePort {

    private static final String ROL_PROPIETARIO = "PROPIETARIO";

    private final IRestaurantePersistencePort persistencePort;
    private final IUsuarioClientPort usuarioClientPort;

    public RestauranteUseCase(IRestaurantePersistencePort persistencePort,
                              IUsuarioClientPort usuarioClientPort) {
        this.persistencePort = persistencePort;
        this.usuarioClientPort = usuarioClientPort;
    }

    @Override
    public Restaurante crearRestaurante(Restaurante r) {
        validarCampos(r);

        if (persistencePort.existePorNit(r.getNit())) {
            throw new DomainException("Ya existe un restaurante con ese NIT");
        }

        String rol = usuarioClientPort.obtenerRol(r.getIdPropietario());
        if (!ROL_PROPIETARIO.equals(rol)) {
            throw new DomainException("El usuario indicado no tiene el rol de propietario");
        }

        return persistencePort.guardar(r);
    }

    private void validarCampos(Restaurante r) {
        if (vacio(r.getNombre()) || vacio(r.getNit()) || vacio(r.getDireccion())
                || vacio(r.getTelefono()) || vacio(r.getUrlLogo())
                || r.getIdPropietario() == null) {
            throw new DomainException("Todos los campos son obligatorios");
        }
        if (!r.getNit().matches("^\\d+$")) {
            throw new DomainException("El NIT debe ser únicamente numérico");
        }
        if (!r.getTelefono().matches("^\\+?\\d{1,12}$")) {
            throw new DomainException("El teléfono debe ser numérico, con máximo 13 caracteres, y puede iniciar con +");
        }
        if (r.getNombre().matches("^\\d+$")) {
            throw new DomainException("El nombre del restaurante no puede contener solo números");
        }
    }

    private boolean vacio(String valor) {
        return valor == null || valor.isBlank();
    }
}