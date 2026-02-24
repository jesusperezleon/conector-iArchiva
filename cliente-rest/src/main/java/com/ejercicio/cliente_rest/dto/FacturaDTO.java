package com.ejercicio.cliente_rest.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FacturaDTO {
    private Long cod_factura;
    private Double importe;
    private LocalDate fecha;
}
