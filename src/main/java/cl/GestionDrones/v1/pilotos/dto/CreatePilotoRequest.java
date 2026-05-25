package cl.GestionDrones.v1.pilotos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import java.time.LocalDate;

public record CreatePilotoRequest(
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
    @Future(message = "La fecha de vencimiento debe ser una fecha futura")
    LocalDate fechaExpiracionCertificado
) {
}
