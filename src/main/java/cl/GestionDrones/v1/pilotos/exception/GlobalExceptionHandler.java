package cl.GestionDrones.v1.pilotos.exception;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public GlobalExceptionHandler() {
        System.out.println("✅ GlobalExceptionHandler de Pilotos REGISTRADO CORRECTAMENTE");
    }

    @ExceptionHandler(CertificadoVencidoException.class)
    public ProblemDetail handleCertificadoVencido(CertificadoVencidoException ex) {
        System.out.println("🔴 ALERTA DE NEGOCIO: Certificado vencido detectado");

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, 
                ex.getMessage()
        );

        problem.setTitle("Certificado DGAC Vencido");
        problem.setProperty("timestamp", Instant.now());
        
        problem.setProperty("num_certificado_afectado", ex.getNumeroCertificadoDgac());
        problem.setProperty("fecha_expiracion_registrada", ex.getFechaVencimientoCertificacion());
        problem.setProperty("dias_desde_vencimiento", 
                java.time.temporal.ChronoUnit.DAYS.between(ex.getFechaVencimientoCertificacion(), LocalDate.now()));
        
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {
        System.out.println("🔴 Error de validación detectado en los datos del Piloto");

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, 
                "Error de validación en los datos enviados"
        );
        problem.setTitle("Validation Error");
        problem.setProperty("timestamp", Instant.now());
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            String campo = error.getField();
            String mensaje = error.getDefaultMessage();
            
            if (mensaje == null) {
                mensaje = "Valor inválido";
            }
            
            errors.put(campo, mensaje);
        }

        problem.setProperty("errors", errors);
        return problem;
    }

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

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException ex) {
        System.out.println("🟡 RECURSO NO ENCONTRADO: " + ex.getMessage());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, 
                ex.getMessage()
        );

        problem.setTitle("Resource Not Found");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarExceptionGeneral(Exception ex) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        respuesta.put("error", "Internal Server Error");
        respuesta.put("mensaje", "Ocurrió un error inesperado en el servidor: " + ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
    }
}