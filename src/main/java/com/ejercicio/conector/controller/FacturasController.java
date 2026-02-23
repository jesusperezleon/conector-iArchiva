package com.ejercicio.conector.controller;

import com.ejercicio.conector.dtos.FacturasProveedorDTO;
import com.ejercicio.conector.models.Factura;
import com.ejercicio.conector.service.FacturasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/facturas")
public class FacturasController {

    @Autowired
    private FacturasService service;
    
    @GetMapping
    public ResponseEntity<FacturasProveedorDTO> getFacturas(
        @RequestParam(required = true ) String CIF,
        @RequestParam(required = false, defaultValue = "") String fechaDesde,
        @RequestParam(required = false, defaultValue = "") String fechaHasta
    ) {
        FacturasProveedorDTO response = service.getFacturas(CIF, fechaDesde, fechaHasta);
        return ResponseEntity.ok(response);
    }
}
