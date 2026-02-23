package com.ejercicio.conector.models;

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.*;

@Entity
@Table(name = "facturas")
public class Factura {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codFactura;

    @ManyToOne
    @JoinColumn(name = "codProveedor", nullable = false)
    private Proveedor proveedor;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "importe", nullable = false)
    private Double importe;

    // Getters y Setters

    public Long getCodFactura() {
        return codFactura;
    }

    public void setCodFactura(Long codFactura) {
        this.codFactura = codFactura;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }   

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }
    
}
