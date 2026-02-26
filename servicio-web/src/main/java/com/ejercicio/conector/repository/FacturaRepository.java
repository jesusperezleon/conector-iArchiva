package com.ejercicio.conector.repository;

import com.ejercicio.conector.entity.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface FacturaRepository extends JpaRepository<Factura, Long> {

    @Query(
            "SELECT f FROM Factura f " +
                    "WHERE f.proveedor.codProveedor = :codProveedor " +
                    "AND (:fechaDesde IS NULL OR f.fecha >= :fechaDesde) " +
                    "AND (:fechaHasta IS NULL OR f.fecha <= :fechaHasta)"
    )
    List<Factura> findAllByFiltros(
            @Param("codProveedor") Long codProveedor,
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta
    );
}