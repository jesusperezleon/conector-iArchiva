package com.ejercicio.conector.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "codProveedor", "cif", "nombre", "email", "facturas" })
public class FacturasProveedorDTO {

    private Long codProveedor;

    @JsonProperty("CIF")
    private String cif;

    private String nombre;
    private String email;
    private List<FacturaDTO> facturas;
}
