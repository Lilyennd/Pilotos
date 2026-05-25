package cl.GestionDrones.v1.pilotos.exception;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * GlobalExceptionHandler unificado y simplificado para la gestión de Pilotos.
 * Utiliza el estándar moderno Problem Details API (RFC 7807) de Spring Boot 3.x.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    public GlobalExceptionHandler() {
        System.out.println("✅ GlobalExceptionHandler de Pilotos REGISTRADO CORRECTAMENTE");
    }

    /**
     * 1. PRIORIDAD MÁXIMA: Maneja cuando un certificado de la DGAC ya expiró
     */
    @ExceptionHandler(CertificadoVencidoException.class)
    public ProblemDetail handleCertificadoVencido(CertificadoVencidoException ex) {
        System.out.println("🔴 ALERTA DE NEGOCIO: Certificado vencido detectado");

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, // Código 409: Conflicto con las reglas de negocio
                ex.getMessage()
        );

        problem.setTitle("Certificado DGAC Vencido");
        problem.setProperty("timestamp", Instant.now());
        
        // Datos específicos e importantes del certificado
        problem.setProperty("num_certificado_afectado", ex.getNumeroCertificadoDgac());
        problem.setProperty("fecha_expiracion_registrada", ex.getFechaVencimientoCertificacion());
        problem.setProperty("dias_desde_vencimiento", 
                java.time.temporal.ChronoUnit.DAYS.between(ex.getFechaVencimientoCertificacion(), LocalDate.now()));
        
        return problem;
    }

    /**
     * 2. VALIDACIÓN SIMPLIFICADA: Traduce los errores de @NotNull, @NotBlank, @FutureOrPresent, etc.
     * Utiliza un bucle 'for' limpio y fácil de entender.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {
        System.out.println("🔴 Error de validación detectado en los datos del Piloto");

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, // Código 400: Petición incorrecta
                "Error de validación en los datos enviados"
        );
        problem.setTitle("Validation Error");
        problem.setProperty("timestamp", Instant.now());

        // Diccionario para guardar los errores estructurados
        Map<String, String> errors = new HashMap<>();

        // Recorremos los errores uno a uno de forma simple
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            String campo = error.getField();
            String mensaje = error.getDefaultMessage();
            
            if (mensaje == null) {
                mensaje = "Valor inválido";
            }
            
            errors.put(campo, mensaje);
        }

        // Añadimos el diccionario al JSON de respuesta
        problem.setProperty("errors", errors);
        return problem;
    }

    /**
     * 3. ERRORES DE PARSEO: Capta cuando el JSON viene mal escrito o las fechas (LocalDate) 
     * no tienen el formato correcto (ej: enviar "25-12-2026" en lugar de "2026-12-25").
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleJsonParseError(HttpMessageNotReadableException ex) {
        System.out.println("🟡 Error de lectura en el JSON enviado");

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Error al procesar el JSON. Asegúrate de que las fechas tengan el formato ISO (AAAA-MM-DD)"
        );

        problem.setTitle("JSON Parse Error");
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("detalle_tecnico", ex.getMostSpecificCause().getMessage());
        return problem;
    }

    /**
     * 4. COMODÍN: Captura cualquier otro error inesperado en el servidor para que la API no se rompa de forma fea.
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralException(Exception ex) {
        System.out.println("❌ EXCEPCIÓN NO CONTROLADA: " + ex.getMessage());
        ex.printStackTrace();

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, // Código 500
                "Ocurrió un error inesperado en el módulo de drones"
        );

        problem.setTitle("Internal Server Error");
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("tipo_excepcion", ex.getClass().getSimpleName());
        return problem;
    }
}