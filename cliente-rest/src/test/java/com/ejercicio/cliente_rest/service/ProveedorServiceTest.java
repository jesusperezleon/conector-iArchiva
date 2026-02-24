package com.ejercicio.cliente_rest.service;

import com.ejercicio.cliente_rest.dto.ProveedorDTO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
class ProveedorServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ProveedorService proveedorService;

    private static final String BASE_URL = "http://localhost:8080";

    @BeforeEach
    void setUp() {
        proveedorService = new ProveedorService(restTemplate, BASE_URL);
    }

    @Test
    @DisplayName("Test unitario 1. CIF válido y existente: el mock devuelve el proveedor esperado")
    void test1_CifValidoYExistente() {
        // ARRANGE: preparamos la respuesta simulada
        String cif = "A12345678";
        ProveedorDTO proveedorEsperado = new ProveedorDTO();
        proveedorEsperado.setCIF(cif);
        proveedorEsperado.setNombre("Proveedor 1");
        proveedorEsperado.setEmail("proveedor@empresa.com");
        proveedorEsperado.setCodProveedor(4L);

        when(restTemplate.getForObject(
                BASE_URL + "/proveedores/" + cif,
                ProveedorDTO.class))
                .thenReturn(proveedorEsperado);

        ProveedorDTO resultado = proveedorService.obtenerPorCif(cif);

        assertNotNull(resultado);
        assertEquals("Proveedor 1", resultado.getNombre());
        assertEquals(4L, resultado.getCodProveedor().longValue());
        assertEquals("proveedor@empresa.com", resultado.getEmail());

        verify(restTemplate, times(1))
                .getForObject(BASE_URL + "/proveedores/" + cif, ProveedorDTO.class);
    }

    // Simulamos que el servidor devuelve 404
    @Test
    @DisplayName("Test unitario 2. CIF inexistente: el mock lanza 404 Not Found")
    void test2_CifInexistente() {

        String cifFalso = "A00000000";

        when(restTemplate.getForObject(
                BASE_URL + "/proveedores/" + cifFalso,
                ProveedorDTO.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // ACT & ASSERT
        HttpClientErrorException ex = assertThrows(
                HttpClientErrorException.class,
                () -> proveedorService.obtenerPorCif(cifFalso)
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    // Simulamos que el servidor rechaza el formato
    @Test
    @DisplayName("Test unitario 3. CIF con formato inválido: el mock lanza 400 Bad Request")
    void test3_CifFormatoInvalido() {

        String cifInvalido = "Z0000X";

        when(restTemplate.getForObject(
                BASE_URL + "/proveedores/" + cifInvalido,
                ProveedorDTO.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        // ACT & ASSERT
        HttpClientErrorException ex = assertThrows(
                HttpClientErrorException.class,
                () -> proveedorService.obtenerPorCif(cifInvalido)
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    @DisplayName("Test unitario 4. CIF vacío: el servicio lanza excepción antes de llamar a la API")
    void test4_CifVacio() {
        // En un test unitario podemos probar la lógica de validación del propio cliente
        // Si el servicio valida el CIF localmente, nunca llega a llamar al RestTemplate
        String cifVacio = "";

        assertThrows(IllegalArgumentException.class,
                () -> proveedorService.obtenerPorCif(cifVacio));

        // Verificamos que con CIF vacío NO se hizo ninguna llamada HTTP
        verifyNoInteractions(restTemplate);
    }

}