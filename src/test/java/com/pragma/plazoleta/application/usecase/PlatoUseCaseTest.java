/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.plazoleta.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.model.CategoriaPlato;
import com.pragma.plazoleta.domain.model.Plato;
import com.pragma.plazoleta.domain.spi.IPlatoPersistencePort;
import com.pragma.plazoleta.domain.spi.IRestaurantePersistencePort;
import java.util.Optional;
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
class PlatoUseCaseTest {

    @Mock
    IPlatoPersistencePort platoPersistencePort;

    @Mock
    IRestaurantePersistencePort restaurantePersistencePort;

    @InjectMocks
    PlatoUseCase useCase;

    private Plato valido() {
        Plato p = new Plato();
        p.setNombre("Bandeja Paisa");
        p.setPrecio(35000L);
        p.setDescripcion("Plato típico colombiano");
        p.setUrlImagen("http://logo.com/bandeja.png");
        p.setCategoria(CategoriaPlato.PLATO_FUERTE);
        p.setIdRestaurante(1L);
        return p;
    }

    @Test
    void creaPlatoConDatosValidosYActivoTrue() {
        when(restaurantePersistencePort.existePorId(1L)).thenReturn(true);
        when(platoPersistencePort.guardar(any())).thenAnswer(i -> i.getArgument(0));

        Plato creado = useCase.crearPlato(valido());

        assertEquals("Bandeja Paisa", creado.getNombre());
        assertEquals(35000L, creado.getPrecio());
        assertEquals(CategoriaPlato.PLATO_FUERTE, creado.getCategoria());
        assertTrue(creado.getActivo());
    }

    @Test
    void rechazaNombreVacio() {
        Plato p = valido();
        p.setNombre(" ");
        assertThrows(DomainException.class, () -> useCase.crearPlato(p));
        verify(platoPersistencePort, never()).guardar(any());
    }

    @Test
    void rechazaDescripcionVacia() {
        Plato p = valido();
        p.setDescripcion("");
        assertThrows(DomainException.class, () -> useCase.crearPlato(p));
    }

    @Test
    void rechazaUrlImagenVacia() {
        Plato p = valido();
        p.setUrlImagen(null);
        assertThrows(DomainException.class, () -> useCase.crearPlato(p));
    }

    @Test
    void rechazaCategoriaNula() {
        Plato p = valido();
        p.setCategoria(null);
        assertThrows(DomainException.class, () -> useCase.crearPlato(p));
    }

    @Test
    void rechazaIdRestauranteNulo() {
        Plato p = valido();
        p.setIdRestaurante(null);
        assertThrows(DomainException.class, () -> useCase.crearPlato(p));
    }

    @Test
    void rechazaPrecioNulo() {
        Plato p = valido();
        p.setPrecio(null);
        assertThrows(DomainException.class, () -> useCase.crearPlato(p));
    }

    @Test
    void rechazaPrecioCero() {
        Plato p = valido();
        p.setPrecio(0L);
        assertThrows(DomainException.class, () -> useCase.crearPlato(p));
    }

    @Test
    void rechazaPrecioNegativo() {
        Plato p = valido();
        p.setPrecio(-100L);
        assertThrows(DomainException.class, () -> useCase.crearPlato(p));
    }

    @Test
    void rechazaRestauranteInexistente() {
        when(restaurantePersistencePort.existePorId(1L)).thenReturn(false);
        assertThrows(DomainException.class, () -> useCase.crearPlato(valido()));
        verify(platoPersistencePort, never()).guardar(any());
    }

    @Test
    void aceptaNombreConLetrasYNumeros() {
        Plato p = valido();
        p.setNombre("Pizza 24 Especial");
        when(restaurantePersistencePort.existePorId(1L)).thenReturn(true);
        when(platoPersistencePort.guardar(any())).thenAnswer(i -> i.getArgument(0));

        Plato creado = useCase.crearPlato(p);

        assertEquals("Pizza 24 Especial", creado.getNombre());
    }

    @Test
    void activoEsTruePorDefecto() {
        Plato p = valido();
        p.setActivo(false); // Aunque el usuario mande false...
        when(restaurantePersistencePort.existePorId(1L)).thenReturn(true);
        when(platoPersistencePort.guardar(any())).thenAnswer(i -> i.getArgument(0));

        Plato creado = useCase.crearPlato(p);

        // ...debe quedar en true
        assertTrue(creado.getActivo());
    }
    
        @Test
    void modificaPrecioYDescripcionCorrectamente() {
        Plato existente = valido();
        existente.setId(1L);
        when(platoPersistencePort.buscarPorId(1L)).thenReturn(Optional.of(existente));
        when(platoPersistencePort.guardar(any())).thenAnswer(i -> i.getArgument(0));

        Plato modificado = useCase.modificarPlato(1L, 50000L, "Nueva descripción");

        assertEquals(50000L, modificado.getPrecio());
        assertEquals("Nueva descripción", modificado.getDescripcion());
        assertEquals("Bandeja Paisa", modificado.getNombre()); // No cambia
        assertEquals(CategoriaPlato.PLATO_FUERTE, modificado.getCategoria()); // No cambia
    }

    @Test
    void rechazaModificarPlatoInexistente() {
        when(platoPersistencePort.buscarPorId(999L)).thenReturn(Optional.empty());

        DomainException ex = assertThrows(DomainException.class,
                () -> useCase.modificarPlato(999L, 50000L, "Nueva descripción"));

        assertEquals("No existe un plato con ese id", ex.getMessage());
        verify(platoPersistencePort, never()).guardar(any());
    }

    @Test
    void rechazaModificarConPrecioCero() {
        assertThrows(DomainException.class,
                () -> useCase.modificarPlato(1L, 0L, "Nueva descripción"));
    }

    @Test
    void rechazaModificarConPrecioNegativo() {
        assertThrows(DomainException.class,
                () -> useCase.modificarPlato(1L, -5000L, "Nueva descripción"));
    }

    @Test
    void rechazaModificarConPrecioNulo() {
        assertThrows(DomainException.class,
                () -> useCase.modificarPlato(1L, null, "Nueva descripción"));
    }

    @Test
    void rechazaModificarConDescripcionVacia() {
        assertThrows(DomainException.class,
                () -> useCase.modificarPlato(1L, 50000L, ""));
    }

    @Test
    void rechazaModificarConDescripcionNula() {
        assertThrows(DomainException.class,
                () -> useCase.modificarPlato(1L, 50000L, null));
    }
}