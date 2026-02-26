//package com.ejercicio.cliente_rest.service;
//
//import com.ejercicio.cliente_rest.dto.ProveedorDTO;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//@Service
//public class ProveedorService {
//    private final RestTemplate restTemplate = new RestTemplate();
//    private final String URL = "http://localhost:8080/proveedores/";
//
//    public ProveedorDTO obtenerPorCif(String cif) {
//        return restTemplate.getForObject(URL + cif, ProveedorDTO.class);
//    }
//}
// src/main/java/com/ejercicio/cliente_rest/service/ProveedorService.java
package com.ejercicio.cliente_rest.service;

import com.ejercicio.cliente_rest.dto.FacturaDTO;
import com.ejercicio.cliente_rest.dto.ProveedorDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ProveedorService {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public ProveedorService(RestTemplate restTemplate,
                            @Value("http://localhost:8080") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public ProveedorDTO obtenerPorCif(String cif) {
        if (cif == null || cif.trim().isEmpty()) {
            throw new IllegalArgumentException("El CIF no puede estar vacío");
        }

        String url = baseUrl + "/proveedores/" + cif;
        return restTemplate.getForObject(url, ProveedorDTO.class);
    }
}