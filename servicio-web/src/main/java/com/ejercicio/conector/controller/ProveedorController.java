package com.ejercicio.conector.controller;

import com.ejercicio.conector.exception.ModelNotFoundException;
import com.ejercicio.conector.exception.ParametrosInvalidosException;
import com.ejercicio.conector.models.Proveedor;
import com.ejercicio.conector.service.ProveedorService;
import com.ejercicio.conector.utils.CifValidator;
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

        if(!CifValidator.isValid(cif)) throw new ParametrosInvalidosException("El formato del CIF es inválido.");

        Proveedor proveedor = service.getProveedor(cif);
        return ResponseEntity.ok(proveedor);
    }
}
