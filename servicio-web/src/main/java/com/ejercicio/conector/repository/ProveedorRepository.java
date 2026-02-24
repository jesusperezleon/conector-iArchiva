package com.ejercicio.conector.repository;

import com.ejercicio.conector.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    Optional<Proveedor> findProveedorByCif(String cif);
}
