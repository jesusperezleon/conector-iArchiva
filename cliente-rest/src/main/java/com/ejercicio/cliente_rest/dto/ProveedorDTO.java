package com.ejercicio.cliente_rest.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProveedorDTO {
    private Long codProveedor;
    private String nombre;
    private String email;
    private String CIF;
}
