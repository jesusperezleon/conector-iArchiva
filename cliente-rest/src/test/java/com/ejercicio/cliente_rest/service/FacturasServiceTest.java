package com.ejercicio.cliente_rest.service;

import com.ejercicio.cliente_rest.dto.FacturaDTO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

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

    private void mockRespuesta(String url, List<FacturaDTO> facturas) {
        when(restTemplate.exchange(
                eq(BASE_URL + "/facturas" + url),
                eq(HttpMethod.GET),
                eq(null),
                any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(facturas));
    }

    private void mockError(String url, HttpStatus status) {
        when(restTemplate.exchange(
                eq(BASE_URL + "/facturas" + url),
                eq(HttpMethod.GET),
                eq(null),
                any(ParameterizedTypeReference.class)))
                .thenThrow(new HttpClientErrorException(status));
    }

    private List<FacturaDTO> buildFacturas(Long... numeros) {
        return Arrays.stream(numeros).map(num -> {
            FacturaDTO f = new FacturaDTO();
            f.setCod_factura(num);
            f.setImporte(1000.00);
            return f;
        }).collect(java.util.stream.Collectors.toList());
    }

    //Casos con CIF

    @Test
    @DisplayName("Test unitario 1. CIF válido sin fechas: retorna facturas del proveedor")
    void test01_CifValidoSinFechas() {
        mockRespuesta("?cif=" + CIF_VALIDO, buildFacturas(4L, 4L));

        List<FacturaDTO> resultado = facturaService.getFacturas(CIF_VALIDO, "", "");

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(restTemplate, times(1)).exchange(
                eq(BASE_URL + "/facturas?cif=" + CIF_VALIDO),
                eq(HttpMethod.GET),
                eq(null),
                any(ParameterizedTypeReference.class));
    }

    @Test
    @DisplayName("Test unitario 2. CIF inexistente: lanza 404 Not Found")
    void test02_CifInexistente() {
        String cifFalso = "A00000000";

        mockError("?cif=" + cifFalso, HttpStatus.NOT_FOUND);

        HttpClientErrorException ex = assertThrows(HttpClientErrorException.class,
                () -> facturaService.getFacturas(cifFalso, "", ""));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    @DisplayName("Test unitario 3. CIF con formato inválido: lanza 400 Bad Request")
    void test03_CifFormatoInvalido() {
        String cifInvalido = "Z0000X";

        mockError("?cif=" + cifInvalido, HttpStatus.BAD_REQUEST);

        HttpClientErrorException ex = assertThrows(HttpClientErrorException.class,
                () -> facturaService.getFacturas(cifInvalido, "", ""));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    @DisplayName("Test unitario 4. CIF vacío: lanza IllegalArgumentException sin llamar al servidor")
    void test04_CifVacio() {
        assertThrows(IllegalArgumentException.class,
                () -> facturaService.getFacturas("", "", ""));

        verifyNoInteractions(restTemplate);
    }

    @Test
    @DisplayName("Test unitario 5. CIF nulo: lanza IllegalArgumentException sin llamar al servidor")
    void test05_CifNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> facturaService.getFacturas(null, "", ""));

        verifyNoInteractions(restTemplate);
    }

    //Casos con fechas

    @Test
    @DisplayName("Test unitario 6. CIF válido con ambas fechas correctas: retorna facturas en el rango")
    void test06_CifValidoConAmbasFechas() {
        String fechaDesde = "2024-01-01";
        String fechaHasta = "2024-12-31";
        String url = "?cif=" + CIF_VALIDO
                + "&fechaDesde=" + fechaDesde
                + "&fechaHasta=" + fechaHasta;

        mockRespuesta(url, buildFacturas(4L));

        List<FacturaDTO> resultado = facturaService.getFacturas(CIF_VALIDO, fechaDesde, fechaHasta);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("Test unitario 7. Solo fechaDesde: retorna facturas desde esa fecha")
    void test07_SoloFechaDesde() {
        String fechaDesde = "2024-01-01";
        String url = "?cif=" + CIF_VALIDO + "&fechaDesde=" + fechaDesde;

        mockRespuesta(url, buildFacturas(4L, 4L));

        List<FacturaDTO> resultado = facturaService.getFacturas(CIF_VALIDO, fechaDesde, "");

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }

    @Test
    @DisplayName("Test unitario 8. Solo fechaHasta: retorna facturas hasta esa fecha")
    void test08_SoloFechaHasta() {
        String fechaHasta = "2024-12-31";
        String url = "?cif=" + CIF_VALIDO + "&fechaHasta=" + fechaHasta;

        mockRespuesta(url, buildFacturas(4L, 4L));

        List<FacturaDTO> resultado = facturaService.getFacturas(CIF_VALIDO, "", fechaHasta);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }

    @Test
    @DisplayName("Test unitario 9. Formato de fechaDesde inválido: lanza IllegalArgumentException sin llamar al servidor")
    void test09_FechaDesdeFormatoInvalido() {
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
}