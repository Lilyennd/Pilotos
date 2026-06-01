package cl.GestionDrones.v1.pilotos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "pilotos")
public class Piloto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "run", nullable = false, unique = true, length = 20)
    private String run;

    @Column(name = "nombres", nullable = false, length = 200)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;


    @Column(name = "telefono", nullable = false, length = 20)
    private String telefono;

    @Column(name = "numero_certificado_dgac", nullable = false, unique = true, length = 50)
    private String numeroCertificadoDgac;

    @Column(name = "fecha_vencimiento_certificacion", nullable = false)
    private LocalDate fechaVencimientoCertificacion;

    public Piloto() {}

    public Piloto(Integer id, String run, String nombres, String apellidos, String telefono, 
                  String numeroCertificadoDgac, LocalDate fechaVencimientoCertificacion) {
        this.id = id;
        this.run = run;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.numeroCertificadoDgac = numeroCertificadoDgac;
        this.fechaVencimientoCertificacion = fechaVencimientoCertificacion;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getRun() { return run; }
    public void setRun(String run) { this.run = run; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getNumeroCertificadoDgac() { return numeroCertificadoDgac; }
    public void setNumeroCertificadoDgac(String numeroCertificadoDgac) { this.numeroCertificadoDgac = numeroCertificadoDgac; }

    public LocalDate getFechaVencimientoCertificacion() { return fechaVencimientoCertificacion; }
    public void setFechaVencimientoCertificacion(LocalDate fechaVencimientoCertificacion) { this.fechaVencimientoCertificacion = fechaVencimientoCertificacion; }
}