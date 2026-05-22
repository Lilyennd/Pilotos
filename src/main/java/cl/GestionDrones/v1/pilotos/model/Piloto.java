package cl.GestionDrones.v1.pilotos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pilotos")
public class Piloto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "run", nullable = false, unique = true, length = 20)
    private String run;

    @Column(name = "nombres", nullable = false, length = 200)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @Column(name = "correo", nullable = false, unique = true, length = 100)
    private String correo;

    @Column(name = "telefono", nullable = false, length = 20)
    private String telefono;

    public Piloto() {}

    public Piloto(int id, String run, String nombres, String apellidos, String correo, String telefono) {
        this.id = id;
        this.run = run;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.telefono = telefono;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getRun() { return run; }
    public void setRun(String run) { this.run = run; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    }