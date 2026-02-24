package com.ejercicio.conector.service;

import com.ejercicio.conector.exception.ModelNotFoundException;
import com.ejercicio.conector.entity.Proveedor;
import com.ejercicio.conector.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProveedorService {

    @Autowired
    private ProveedorRepository repository;

    public Proveedor getProveedor(String CIF){
        Optional<Proveedor> proveedor = repository.findProveedorByCif(CIF);

        if(!proveedor.isPresent()){
            throw new ModelNotFoundException("No existe ningun proveedor con ese CIF");
        }

        return proveedor.get();
    }
}
