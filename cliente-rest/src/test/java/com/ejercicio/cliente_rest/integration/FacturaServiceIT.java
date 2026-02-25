package com.ejercicio.cliente_rest.integration;

import com.ejercicio.cliente_rest.dto.FacturaDTO;
import com.ejercicio.cliente_rest.service.FacturaService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class FacturaServiceIT {

    @Autowired
    private FacturaService facturaService;

    private static final String CIF_VALIDO = "D45678901";

    @Test
    @DisplayName("Test integración 1. CIF válido sin fechas: retorna facturas")
    void test01_CifValidoSinFechas() {
        List<FacturaDTO> resultado = facturaService.getFacturas(CIF_VALIDO, "", "");
        assertNotNull(resultado);
    }

    @Test
    @DisplayName("Test integración 2. CIF inexistente: retorna 404")
    void test02_CifInexistente() {
        assertThrows(HttpClientErrorException.NotFound.class,
                () -> facturaService.getFacturas("A00000000", "", ""));
    }

    @Test
    @DisplayName("Test integración 3. CIF con formato inválido: retorna 400")
    void test03_CifFormatoInvalido() {
        assertThrows(HttpClientErrorException.BadRequest.class,
                () -> facturaService.getFacturas("Z0000X", "", ""));
    }

    @Test
    @DisplayName("Test integración 4. CIF vacío: lanza IllegalArgumentException")
    void test04_CifVacio() {
        assertThrows(IllegalArgumentException.class,
                () -> facturaService.getFacturas("", "", ""));
    }

    @Test
    @DisplayName("Test integración 5. CIF válido con ambas fechas correctas: retorna facturas")
    void test05_AmbasFechasCorrectas() {
        List<FacturaDTO> resultado = facturaService.getFacturas(CIF_VALIDO, "2024-01-01", "2024-12-31");
        assertNotNull(resultado);
    }

    @Test
    @DisplayName("Test integración 6. Solo fechaDesde: retorna facturas desde esa fecha")
    void test06_SoloFechaDesde() {
        List<FacturaDTO> resultado = facturaService.getFacturas(CIF_VALIDO, "2024-01-01", "");
        assertNotNull(resultado);
    }

    @Test
    @DisplayName("Test integración 7. Solo fechaHasta: retorna facturas hasta esa fecha")
    void test07_SoloFechaHasta() {
        List<FacturaDTO> resultado = facturaService.getFacturas(CIF_VALIDO, "", "2024-12-31");
        assertNotNull(resultado);
    }

    @Test
    @DisplayName("Test integración 8. Formato fechaDesde inválido: lanza IllegalArgumentException")
    void test08_FechaDesdeFormatoInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> facturaService.getFacturas(CIF_VALIDO, "01-01-2024", ""));
    }

    @Test
    @DisplayName("Test integración 9. Formato fechaHasta inválido: lanza IllegalArgumentException")
    void test09_FechaHastaFormatoInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> facturaService.getFacturas(CIF_VALIDO, "", "31/12/2024"));
    }

    @Test
    @DisplayName("Test integración 10. fechaDesde posterior a fechaHasta: lanza IllegalArgumentException")
    void test10_FechaDesdePostiorAFechaHasta() {
        assertThrows(IllegalArgumentException.class,
                () -> facturaService.getFacturas(CIF_VALIDO, "2024-12-31", "2024-01-01"));
    }
}
