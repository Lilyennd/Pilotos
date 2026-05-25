package cl.GestionDrones.v1.pilotos.exception;

import java.time.LocalDate;

public class CertificadoVencidoException extends RuntimeException {
    private final String numeroCertificadoDgac;
    private final LocalDate fechaVencimientoCertificacion;

    public CertificadoVencidoException(String message, String numeroCertificadoDgac, LocalDate fechaVencimientoCertificacion) {
        super(message);
        this.numeroCertificadoDgac = numeroCertificadoDgac;
        this.fechaVencimientoCertificacion = fechaVencimientoCertificacion;
    }

    public String getNumeroCertificadoDgac() { 
        return numeroCertificadoDgac; 
    }
    
    public LocalDate getFechaVencimientoCertificacion() { 
        return fechaVencimientoCertificacion; 
    }
}