/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.plazoleta.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.model.Restaurante;
import com.pragma.plazoleta.domain.spi.IRestaurantePersistencePort;
import com.pragma.plazoleta.domain.spi.IUsuarioClientPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
/**
 *
 * @author Juan Camilo
 */
@ExtendWith(MockitoExtension.class)
class RestauranteUseCaseTest {

    @Mock
    IRestaurantePersistencePort persistencePort;

    @Mock
    IUsuarioClientPort usuarioClientPort;

    @InjectMocks
    RestauranteUseCase useCase;

    private Restaurante valido() {
        Restaurante r = new Restaurante();
        r.setNombre("Pizzería Roma");
        r.setNit("900123456");
        r.setDireccion("Calle 10 # 5-20");
        r.setTelefono("+573005698325");
        r.setUrlLogo("http://logo.com/roma.png");
        r.setIdPropietario(1L);
        return r;
    }

    @Test
    void creaRestauranteCuandoElPropietarioEsValido() {
        when(usuarioClientPort.obtenerRol(1L)).thenReturn("PROPIETARIO");
        when(persistencePort.guardar(any())).thenAnswer(i -> i.getArgument(0));

        Restaurante creado = useCase.crearRestaurante(valido());

        assertEquals("Pizzería Roma", creado.getNombre());
    }

    @Test
    void rechazaCamposVacios() {
        Restaurante r = valido();
        r.setNombre(" ");
        assertThrows(DomainException.class, () -> useCase.crearRestaurante(r));
    }

    @Test
    void rechazaNitConLetrasSinConsultarUsuarios() {
        Restaurante r = valido();
        r.setNit("90012ab");
        assertThrows(DomainException.class, () -> useCase.crearRestaurante(r));
        verifyNoInteractions(usuarioClientPort);
    }

    @Test
    void rechazaTelefonoInvalido() {
        Restaurante r = valido();
        r.setTelefono("+5730056983250000");
        assertThrows(DomainException.class, () -> useCase.crearRestaurante(r));
    }

    @Test
    void rechazaNombreSoloNumeros() {
        Restaurante r = valido();
        r.setNombre("12345");
        assertThrows(DomainException.class, () -> useCase.crearRestaurante(r));
    }

    @Test
    void aceptaNombreConLetrasYNumeros() {
        Restaurante r = valido();
        r.setNombre("Pizza 24");
        when(usuarioClientPort.obtenerRol(1L)).thenReturn("PROPIETARIO");
        when(persistencePort.guardar(any())).thenAnswer(i -> i.getArgument(0));

        assertEquals("Pizza 24", useCase.crearRestaurante(r).getNombre());
    }

    @Test
    void rechazaNitDuplicado() {
        when(persistencePort.existePorNit("900123456")).thenReturn(true);
        assertThrows(DomainException.class, () -> useCase.crearRestaurante(valido()));
    }

    @Test
    void rechazaUsuarioQueNoEsPropietario() {
        when(usuarioClientPort.obtenerRol(1L)).thenReturn("CLIENTE");
        assertThrows(DomainException.class, () -> useCase.crearRestaurante(valido()));
        verify(persistencePort, never()).guardar(any());
    }
}