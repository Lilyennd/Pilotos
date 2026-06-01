package cl.GestionDrones.v1.pilotos.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;


public record CreatePilotoRequest(
    @NotBlank(message = "El RUN no puede estar vacío") 
    String run,
    
    @NotBlank(message = "El nombre no puede estar vacío") 
    String nombres,    
    
    @NotBlank(message = "El apellido no puede estar vacío") 
    String apellidos,  

    @NotBlank(message = "El teléfono no puede estar vacío") 
    String telefono,
    
    @NotBlank(message = "El número de certificado de la DGAC no puede estar vacío") 
    String numeroCertificadoDgac,
    
    @NotNull(message = "La fecha de vencimiento de la certificación es obligatoria") 
    @Future(message = "El certificado médico/vuelo ya está vencido o no está vigente")
    LocalDate fechaVencimientoCertificacion 
) {}
