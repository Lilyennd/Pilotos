package cl.GestionDrones.v1.pilotos.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdatePilotoRquest(
    @NotBlank(message = "El RUN no puede ser vacío") 
    String run,

    @NotBlank(message = "El nombre no puede ser vacío") 
    String nombre,

    @NotBlank(message = "El apellido no puede ser vacío") 
    String apellido,

    @NotBlank(message = "El correo no puede ser vacío")
    String correo,

    @NotBlank(message = "El teléfono no puede ser vacío") 
    String telefono,

    @NotBlank(message = "El número de certificado de la DGAC no puede ser vacío") 
    String numeroCertificadoDgac,

    @NotNull(message = "La fecha de vencimiento de la certificación es obligatoria") 
    LocalDate fechaExpiracionCertificado
) {
}