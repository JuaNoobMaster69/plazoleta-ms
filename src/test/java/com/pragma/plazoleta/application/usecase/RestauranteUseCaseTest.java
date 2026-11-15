package com.pragma.plazoleta.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.model.Pagina;
import com.pragma.plazoleta.domain.model.Restaurante;
import com.pragma.plazoleta.domain.spi.IRestaurantePersistencePort;
import com.pragma.plazoleta.domain.spi.IUsuarioClientPort;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    // =========================================================
    // HU2: crearRestaurante
    // =========================================================
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

    // =========================================================
    // HU9: listarRestaurantes (Corregido con Pagina)
    // =========================================================
    @Test
    void listarRestaurantes_conParametrosValidos_devuelvePagina() {
        Restaurante r1 = valido();
        r1.setNombre("Pizza 24");
        Restaurante r2 = valido();
        r2.setNombre("Pizzería Roma");
        List<Restaurante> lista = Arrays.asList(r1, r2);

        Pagina<Restaurante> paginaEsperada = new Pagina<>(lista, 0, 10, 2);

        when(persistencePort.listarPaginado(0, 10)).thenReturn(paginaEsperada);

        Pagina<Restaurante> resultado = useCase.listarRestaurantes(0, 10);

        assertEquals(2, resultado.contenido().size());
        assertEquals("Pizza 24", resultado.contenido().get(0).getNombre());
        assertEquals(10, resultado.tamano());
    }

    @Test
    void listarRestaurantes_conPageNegativo_lanzaExcepcion() {
        DomainException ex = assertThrows(DomainException.class,
                () -> useCase.listarRestaurantes(-1, 10));

        assertEquals("El número de página no puede ser negativo", ex.getMessage());
    }

    @Test
    void listarRestaurantes_conTamanoCero_lanzaExcepcion() {
        DomainException ex = assertThrows(DomainException.class,
                () -> useCase.listarRestaurantes(0, 0));

        assertEquals("El tamaño de la página debe ser al menos 1", ex.getMessage());
    }
}
