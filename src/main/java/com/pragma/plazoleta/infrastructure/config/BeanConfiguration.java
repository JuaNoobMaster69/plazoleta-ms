/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.plazoleta.infrastructure.config;

import com.pragma.plazoleta.application.usecase.RestauranteUseCase;
import com.pragma.plazoleta.domain.api.IRestauranteServicePort;
import com.pragma.plazoleta.domain.spi.IRestaurantePersistencePort;
import com.pragma.plazoleta.domain.spi.IUsuarioClientPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 *
 * @author Juan Camilo
 */
@Configuration
public class BeanConfiguration {

    @Bean
    public IRestauranteServicePort restauranteServicePort(IRestaurantePersistencePort persistencePort,
                                                          IUsuarioClientPort usuarioClientPort) {
        return new RestauranteUseCase(persistencePort, usuarioClientPort);
    }
}