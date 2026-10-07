/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.plazoleta.application.usecase;

import com.pragma.plazoleta.domain.api.IPlatoServicePort;
import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.model.Plato;
import com.pragma.plazoleta.domain.spi.IPlatoPersistencePort;
import com.pragma.plazoleta.domain.spi.IRestaurantePersistencePort;

/**
 *
 * @author Juan Camilo
 */
public class PlatoUseCase implements IPlatoServicePort {

    private static final long PRECIO_MINIMO = 1L;

    private final IPlatoPersistencePort platoPersistencePort;
    private final IRestaurantePersistencePort restaurantePersistencePort;

    public PlatoUseCase(IPlatoPersistencePort platoPersistencePort,
                        IRestaurantePersistencePort restaurantePersistencePort) {
        this.platoPersistencePort = platoPersistencePort;
        this.restaurantePersistencePort = restaurantePersistencePort;
    }

    @Override
    public Plato crearPlato(Plato plato) {
        validarCampos(plato);
        validarPrecio(plato.getPrecio());
        validarRestaurante(plato.getIdRestaurante());

        plato.setActivo(true);

        return platoPersistencePort.guardar(plato);
    }

    private void validarCampos(Plato p) {
        if (vacio(p.getNombre()) || vacio(p.getDescripcion())
                || vacio(p.getUrlImagen()) || p.getCategoria() == null
                || p.getIdRestaurante() == null) {
            throw new DomainException("Todos los campos son obligatorios");
        }
        if (p.getPrecio() == null) {
            throw new DomainException("El precio es obligatorio");
        }
    }

    private void validarPrecio(Long precio) {
        if (precio < PRECIO_MINIMO) {
            throw new DomainException("El precio debe ser un número entero positivo mayor a 0");
        }
    }

    private void validarRestaurante(Long idRestaurante) {
        if (!restaurantePersistencePort.existePorId(idRestaurante)) {
            throw new DomainException("El restaurante indicado no existe");
        }
    }

    private boolean vacio(String valor) {
        return valor == null || valor.isBlank();
    }
}