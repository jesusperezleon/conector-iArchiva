package com.ejercicio.cliente_rest.service;

import com.ejercicio.cliente_rest.dto.FacturaDTO;
import com.ejercicio.cliente_rest.dto.FacturasProveedorDTO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
class FacturaServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private FacturaService facturaService;

    private static final String BASE_URL = "http://localhost:8080";
    private static final String CIF_VALIDO = "D45678901";

    @BeforeEach
    void setUp() {
        facturaService = new FacturaService(restTemplate, BASE_URL);
    }

    // --- Casos con CIF ---

    @Test
    @DisplayName("Test unitario 1. CIF válido sin fechas: retorna facturas del proveedor")
    void test1_CifValidoSinFechas() {
        FacturasProveedorDTO mockResponse = buildResponse(CIF_VALIDO, 4L, 4L);

        when(restTemplate.getForObject(
                BASE_URL + "/facturas?cif=" + CIF_VALIDO,
                FacturasProveedorDTO.class))
                .thenReturn(mockResponse);

        FacturasProveedorDTO resultado = facturaService.getFacturas(CIF_VALIDO, "", "");

        assertNotNull(resultado);
        assertEquals(2, resultado.getFacturas().size());
        verify(restTemplate, times(1))
                .getForObject(BASE_URL + "/facturas?cif=" + CIF_VALIDO, FacturasProveedorDTO.class);
    }

    @Test
    @DisplayName("Test unitario 2. CIF inexistente: lanza 404 Not Found")
    void test2_CifInexistente() {
        String cifFalso = "A00000000";

        when(restTemplate.getForObject(
                BASE_URL + "/facturas?cif=" + cifFalso,
                FacturasProveedorDTO.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        HttpClientErrorException ex = assertThrows(HttpClientErrorException.class,
                () -> facturaService.getFacturas(cifFalso, "", ""));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    @DisplayName("Test unitario 3. CIF con formato inválido: lanza 400 Bad Request")
    void test3_CifFormatoInvalido() {
        String cifInvalido = "Z0000X";

        when(restTemplate.getForObject(
                BASE_URL + "/facturas?cif=" + cifInvalido,
                FacturasProveedorDTO.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        HttpClientErrorException ex = assertThrows(HttpClientErrorException.class,
                () -> facturaService.getFacturas(cifInvalido, "", ""));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    @DisplayName("Test unitario 4. CIF vacío: lanza IllegalArgumentException sin llamar al servidor")
    void test4_CifVacio() {
        assertThrows(IllegalArgumentException.class,
                () -> facturaService.getFacturas("", "", ""));

        verifyNoInteractions(restTemplate);
    }

    @Test
    @DisplayName("Test unitario 5. CIF nulo: lanza IllegalArgumentException sin llamar al servidor")
    void test5_CifNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> facturaService.getFacturas(null, "", ""));

        verifyNoInteractions(restTemplate);
    }

    // --- Casos con fechas ---

    @Test
    @DisplayName("Test unitario 6. CIF válido con ambas fechas correctas: retorna facturas en el rango")
    void test6_CifValidoConAmbasFechas() {
        String fechaDesde = "2024-01-01";
        String fechaHasta = "2024-12-31";
        String url = BASE_URL + "/facturas?cif=" + CIF_VALIDO
                + "&fechaDesde=" + fechaDesde
                + "&fechaHasta=" + fechaHasta;

        FacturasProveedorDTO mockResponse = buildResponse(CIF_VALIDO, 4L);

        when(restTemplate.getForObject(url, FacturasProveedorDTO.class))
                .thenReturn(mockResponse);

        FacturasProveedorDTO resultado = facturaService.getFacturas(CIF_VALIDO, fechaDesde, fechaHasta);

        assertNotNull(resultado);
        assertEquals(1, resultado.getFacturas().size());
    }

    @Test
    @DisplayName("Test unitario 7. Solo fechaDesde: retorna facturas desde esa fecha")
    void test7_SoloFechaDesde() {
        String fechaDesde = "2024-01-01";
        String url = BASE_URL + "/facturas?cif=" + CIF_VALIDO + "&fechaDesde=" + fechaDesde;

        FacturasProveedorDTO mockResponse = buildResponse(CIF_VALIDO, 4L, 4L);

        when(restTemplate.getForObject(url, FacturasProveedorDTO.class))
                .thenReturn(mockResponse);

        FacturasProveedorDTO resultado = facturaService.getFacturas(CIF_VALIDO, fechaDesde, "");

        assertNotNull(resultado);
        assertEquals(2, resultado.getFacturas().size());
    }

    @Test
    @DisplayName("Test unitario 8. Solo fechaHasta: retorna facturas hasta esa fecha")
    void test8_SoloFechaHasta() {
        String fechaHasta = "2024-12-31";
        String url = BASE_URL + "/facturas?cif=" + CIF_VALIDO + "&fechaHasta=" + fechaHasta;

        FacturasProveedorDTO mockResponse = buildResponse(CIF_VALIDO, 4L);

        when(restTemplate.getForObject(url, FacturasProveedorDTO.class))
                .thenReturn(mockResponse);

        FacturasProveedorDTO resultado = facturaService.getFacturas(CIF_VALIDO, "", fechaHasta);

        assertNotNull(resultado);
        assertEquals(1, resultado.getFacturas().size());
    }

    @Test
    @DisplayName("Test unitario 9. Formato de fechaDesde inválido: lanza IllegalArgumentException sin llamar al servidor")
    void test9_FechaDesdeFormatoInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> facturaService.getFacturas(CIF_VALIDO, "01-01-2024", ""));

        verifyNoInteractions(restTemplate);
    }

    @Test
    @DisplayName("Test unitario 10. Formato de fechaHasta inválido: lanza IllegalArgumentException sin llamar al servidor")
    void test10_FechaHastaFormatoInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> facturaService.getFacturas(CIF_VALIDO, "", "31/12/2024"));

        verifyNoInteractions(restTemplate);
    }

    @Test
    @DisplayName("Test unitario 11. fechaDesde posterior a fechaHasta: lanza IllegalArgumentException sin llamar al servidor")
    void test11_FechaDesdePostiorAFechaHasta() {
        assertThrows(IllegalArgumentException.class,
                () -> facturaService.getFacturas(CIF_VALIDO, "2024-12-31", "2024-01-01"));

        verifyNoInteractions(restTemplate);
    }

    // --- Método auxiliar para construir respuestas mock ---
    private FacturasProveedorDTO buildResponse(String cif, Long... numeroFacturas) {
        FacturasProveedorDTO response = new FacturasProveedorDTO();
        response.setCif(cif);

        response.setFacturas(Arrays.stream(numeroFacturas).map(num -> {
            FacturaDTO f = new FacturaDTO();
            f.setCod_factura(num);
            f.setImporte(1000.00);
            return f;
        }).collect(java.util.stream.Collectors.toList()));
        return response;
    }
}