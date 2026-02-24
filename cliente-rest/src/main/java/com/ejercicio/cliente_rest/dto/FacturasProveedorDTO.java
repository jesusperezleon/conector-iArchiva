package com.ejercicio.cliente_rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "codProveedor", "cif", "nombre", "email", "facturas" })
@Getter
@Setter
public class FacturasProveedorDTO {

    private Long codProveedor;

    @JsonProperty("CIF")
    private String cif;

    private String nombre;
    private String email;
    private List<FacturaDTO> facturas;
}
