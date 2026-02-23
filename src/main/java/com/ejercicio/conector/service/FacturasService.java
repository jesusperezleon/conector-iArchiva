package com.ejercicio.conector.service;

import com.ejercicio.conector.dtos.FacturaDTO;
import com.ejercicio.conector.dtos.FacturasProveedorDTO;
import com.ejercicio.conector.exception.LogicaErrorException;
import com.ejercicio.conector.models.Factura;
import com.ejercicio.conector.models.Proveedor;
import com.ejercicio.conector.repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacturasService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private ProveedorService proveedorService;

    public FacturasProveedorDTO getFacturas(String cif, String fechaDesdeStr , String fechaHastaStr ){

        //Obtemos el proveedor a partir del CIF
        Proveedor proveedor = proveedorService.getProveedor(cif);

        //Pasamos las fechas de String a LocalDate y validamos
        LocalDate fechaDesde = null;
        if (fechaDesdeStr != null && !fechaDesdeStr.isEmpty()) {
            fechaDesde = LocalDate.parse(fechaDesdeStr); // Asume formato yyyy-MM-dd
        }

        LocalDate fechaHasta = null;
        if (fechaHastaStr != null && !fechaHastaStr.isEmpty()) {
            fechaHasta = LocalDate.parse(fechaHastaStr);
        }

        if (fechaDesde != null && fechaHasta != null){
            if( fechaDesde.isAfter(fechaHasta) ) {
                throw new LogicaErrorException("El parametro fechaDesde no puede ser posterior a fechaHasta");
            }
        }

        // Obtenemos las facturas
        List<Factura> facturas = facturaRepository.findAllByFiltros(proveedor.getCodProveedor(), fechaDesde, fechaHasta);

        return new FacturasProveedorDTO(
            proveedor.getCodProveedor(),
            proveedor.getCif(),
            proveedor.getNombre(),
            proveedor.getEmail(),
            facturas.stream().map(factura ->
                new FacturaDTO(
                        factura.getCodFactura(),
                        factura.getImporte(),
                        factura.getFecha()
                )
            ).collect(Collectors.toList())
        );
    }
}
