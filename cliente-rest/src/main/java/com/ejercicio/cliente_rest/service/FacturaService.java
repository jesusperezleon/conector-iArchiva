package com.ejercicio.cliente_rest.service;

import com.ejercicio.cliente_rest.dto.FacturaDTO;
import com.ejercicio.cliente_rest.utils.DateValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class FacturaService {
    private final RestTemplate restTemplate;
    private final String baseUrl;

    public FacturaService(RestTemplate restTemplate,
                          @Value("http://localhost:8080") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public List<FacturaDTO> getFacturas(String cif, String fechaDesde, String fechaHasta) {

        // Validaciones en el cliente antes de llamar al servidor
        if (cif == null || cif.trim().isEmpty())
            throw new IllegalArgumentException("El CIF es obligatorio");

        if (fechaDesde != null && !fechaDesde.isEmpty() && !DateValidator.isValid(fechaDesde))
            throw new IllegalArgumentException("Formato de fechaDesde inválido, use AAAA-MM-DD");

        if (fechaHasta != null && !fechaHasta.isEmpty() && !DateValidator.isValid(fechaHasta))
            throw new IllegalArgumentException("Formato de fechaHasta inválido, use AAAA-MM-DD");

        if (fechaDesde != null && !fechaDesde.isEmpty()
                && fechaHasta != null && !fechaHasta.isEmpty()
                && fechaDesde.compareTo(fechaHasta) > 0)
            throw new IllegalArgumentException("fechaDesde no puede ser posterior a fechaHasta");

        // Construimos la URL con los parámetros opcionales
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(baseUrl + "/facturas")
                .queryParam("cif", cif);

        if (fechaDesde != null && !fechaDesde.isEmpty())
            builder.queryParam("fechaDesde", fechaDesde);

        if (fechaHasta != null && !fechaHasta.isEmpty())
            builder.queryParam("fechaHasta", fechaHasta);

        String url = builder.toUriString();
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<FacturaDTO>>() {}
        ).getBody();
    }
}
