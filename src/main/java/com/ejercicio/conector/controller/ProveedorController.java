package com.ejercicio.conector.controller;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/proveedores")
public class ProveedorController {
    
    @GetMapping("/{cif}")
    public String getProveedorByCif(@PathVariable String cif) {
        // Aquí puedes implementar la lógica para obtener el proveedor por su CIF
        return "Proveedor con CIF: " + cif;
    }
}
