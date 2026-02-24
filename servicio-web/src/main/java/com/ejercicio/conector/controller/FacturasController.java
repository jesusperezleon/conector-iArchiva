package com.ejercicio.conector.controller;

import com.ejercicio.conector.dtos.FacturasProveedorDTO;
import com.ejercicio.conector.exception.ParametrosInvalidosException;
import com.ejercicio.conector.service.FacturasService;
import com.ejercicio.conector.utils.CifValidator;
import com.ejercicio.conector.utils.DateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/facturas")
public class FacturasController {

    @Autowired
    private FacturasService service;
    
    @GetMapping
    public ResponseEntity<FacturasProveedorDTO> getFacturas(
        @RequestParam(required = true ) String cif,
        @RequestParam(required = false, defaultValue = "") String fechaDesde,
        @RequestParam(required = false, defaultValue = "") String fechaHasta
    ) {
        if(!CifValidator.isValid(cif)) throw new ParametrosInvalidosException("El formato del CIF es inválido.");

        if(!fechaDesde.isEmpty() || !fechaHasta.isEmpty()){
            // Validar formato de fecha
            if(!fechaDesde.isEmpty() && !DateValidator.isValid(fechaDesde))
                throw new ParametrosInvalidosException("Formato de fechaDesde inválido, use AAAA-MM-DD");

            if(!fechaHasta.isEmpty() && !DateValidator.isValid(fechaHasta))
                throw new ParametrosInvalidosException("Formato de fechaHasta inválido, use AAAA-MM-DD");
        }

        FacturasProveedorDTO response = service.getFacturas(cif, fechaDesde, fechaHasta);
        return ResponseEntity.ok(response);
    }
}
