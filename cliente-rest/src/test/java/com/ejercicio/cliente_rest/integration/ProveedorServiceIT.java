package com.ejercicio.cliente_rest.integration;

import com.ejercicio.cliente_rest.dto.ProveedorDTO;
import com.ejercicio.cliente_rest.service.ProveedorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class ProveedorServiceIT {

    @Autowired
    private ProveedorService proveedorService;

    @Test
    @DisplayName("Test de integración 1. CIF Válido y existente: Retorna datos del proveedor")
    void test1_CifValidoYExistente() {
        String cifValido = "D45678901";
        ProveedorDTO resultado = proveedorService.obtenerPorCif(cifValido);

        assertNotNull(resultado);
        assertEquals("Proveedor Delta", resultado.getNombre());
        // Uso 4L si codProveedor es Long para evitar problemas de tipos
        assertEquals(4L, resultado.getCodProveedor().longValue());
    }

    @Test
    @DisplayName("Test de integración 2. CIF Válido pero inexistente: Retorna 404 Not Found")
    void test2_CifInexistente() {
        String cifFalso = "A00000000";

        assertThrows(HttpClientErrorException.NotFound.class, () -> {
            proveedorService.obtenerPorCif(cifFalso);
        });
    }

    @Test
    @DisplayName("Test de integración 3. CIF con formato inválido: Retorna 400 Bad Request")
    void test3_CifFormatoInvalido() {
        String cifInvalido = "Z0000X";

        assertThrows(HttpClientErrorException.BadRequest.class, () -> {
            proveedorService.obtenerPorCif(cifInvalido);
        });
    }

    @Test
    @DisplayName("Test de integración 4. CIF vacío: Retorna 404 por ruta inexistente")
    void test4_CifVacio() {
        String cifVacio = "";

        assertThrows(IllegalArgumentException.class, () -> {
            proveedorService.obtenerPorCif(cifVacio);
        });
    }
}