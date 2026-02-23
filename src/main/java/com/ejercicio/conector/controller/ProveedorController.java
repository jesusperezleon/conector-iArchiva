package com.ejercicio.conector.controller;

import com.ejercicio.conector.exception.ModelNotFoundException;
import com.ejercicio.conector.exception.ParametrosInvalidosException;
import com.ejercicio.conector.models.Proveedor;
import com.ejercicio.conector.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService service;
    
    @GetMapping("/{cif}")
    public ResponseEntity<Proveedor> getProveedorByCif(@PathVariable String cif) {
        Proveedor proveedor = service.getProveedor(cif);
        return ResponseEntity.ok(proveedor);
    }
}
