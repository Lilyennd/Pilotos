package cl.GestionDrones.v1.pilotos.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.GestionDrones.v1.pilotos.dto.CreatePilotoRequest;
import cl.GestionDrones.v1.pilotos.dto.UpdatePilotoRequest;
import cl.GestionDrones.v1.pilotos.model.Piloto;
import cl.GestionDrones.v1.pilotos.service.PilotosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Pilotos", description = "Operaciones relacionadas con la gestión de pilotos")
@RestController
@RequestMapping("/api/v1/pilotos")
public class PilotosController {

    private final PilotosService pilotosService;

    public PilotosController(PilotosService pilotosService) {
        this.pilotosService = pilotosService;
    }

    @Operation(summary = "Crear un nuevo piloto", description = "Registra un piloto en el sistema con validación de datos")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Estructura JSON del nuevo piloto a registrar",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = CreatePilotoRequest.class),
            examples = @ExampleObject(
                name = "Ejemplo de Nuevo Piloto",
                value = "{\n  \"run\": \"12345678-9\",\n  \"nombres\": \"Juan Alberto\",\n  \"apellidos\": \"Pérez González\",\n  \"telefono\": \"+56912345678\",\n  \"numeroCertificadoDgac\": \"DGAC-123456\",\n  \"fechaVencimientoCertificacion\": \"2027-08-24\"\n}"
            )
        )
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Piloto registrado exitosamente",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Piloto.class))),
        @ApiResponse(responseCode = "400", description = "Existen errores de validación en los datos del piloto", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno al guardar el piloto", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(
        @Valid @RequestBody CreatePilotoRequest request,
        BindingResult result) {

        if (result.hasErrors()) {
            return manejarErrores(result);
        }

        Piloto nuevoPiloto = pilotosService.savePiloto(request);

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.CREATED.value());
        response.put("mensaje", "Piloto registrado exitosamente");
        response.put("datos", nuevoPiloto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener todos los pilotos", description = "Retorna una lista con todos los pilotos registrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pilotos obtenida con éxito",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Piloto.class)))
    })
    @GetMapping
    public ResponseEntity<List<Piloto>> listarPilotos() {
        List<Piloto> pilotos = pilotosService.getAllPilotos();
        return ResponseEntity.ok(pilotos);
    }

    @Operation(summary = "Obtener piloto por ID", description = "Busca y retorna los detalles de un piloto mediante su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Piloto encontrado",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Piloto.class))),
        @ApiResponse(responseCode = "404", description = "No se encontró ningún piloto con el ID proporcionado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerPorId(
        @Parameter(description = "ID del piloto a buscar", required = true, example = "1")
        @PathVariable Integer id
    ) {
        Piloto piloto = pilotosService.getPilotoById(id);
        Map<String, Object> response = new HashMap<>();

        if (piloto == null) {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("problema", "No se encontró ningún piloto con el ID proporcionado");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        response.put("status", HttpStatus.OK.value());
        response.put("mensaje", "Piloto encontrado");
        response.put("datos", piloto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Actualizar piloto", description = "Modifica los datos de un piloto existente usando su ID")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Estructura JSON con los nuevos datos del piloto",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = UpdatePilotoRequest.class),
            examples = @ExampleObject(
                name = "Ejemplo de Actualización de Piloto",
                value = "{\n  \"nombres\": \"Juan Alberto\",\n  \"apellidos\": \"Pérez Silva\",\n  \"telefono\": \"+56998765432\",\n  \"numeroCertificadoDgac\": \"DGAC-123456-MOD\",\n  \"fechaVencimientoCertificacion\": \"2028-11-15\"\n}"
            )
        )
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Datos del piloto actualizados correctamente",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Piloto.class))),
        @ApiResponse(responseCode = "400", description = "Existen errores de validación en los datos del piloto", content = @Content),
        @ApiResponse(responseCode = "404", description = "No se encontró ningún piloto con el ID proporcionado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(
        @Parameter(description = "ID del piloto que se desea actualizar", required = true, example = "1")
        @PathVariable Integer id, 
        @Valid @RequestBody UpdatePilotoRequest request, 
        BindingResult result) {
            
        if (result.hasErrors()) {
            return manejarErrores(result);
        }

        Piloto pilotoActualizado = pilotosService.updatePiloto(id, request);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("mensaje", "Datos del piloto actualizados correctamente");
        response.put("datos", pilotoActualizado);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Eliminar un piloto", description = "Elimina permanentemente un piloto del sistema usando su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Piloto eliminado del sistema correctamente", content = @Content),
        @ApiResponse(responseCode = "404", description = "No se encontró ningún piloto con el ID proporcionado para eliminar", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(
        @Parameter(description = "ID del piloto que se desea eliminar", required = true, example = "1")
        @PathVariable Integer id
    ) {
        Map<String, Object> response = new HashMap<>();
        boolean eliminado = pilotosService.deletePiloto(id); 
        
        if (!eliminado) {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("problema", "No se encontró ningún piloto con el ID proporcionado para eliminar");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        
        response.put("status", HttpStatus.OK.value());
        response.put("mensaje", "Piloto eliminado del sistema correctamente");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Obtener piloto por RUN", description = "Busca un piloto por su número de RUN único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Piloto encontrado",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Piloto.class))),
        @ApiResponse(responseCode = "404", description = "Piloto no encontrado", content = @Content)
    })
    @GetMapping("/run/{run}")
    public ResponseEntity<?> obtenerPorRun(
        @Parameter(description = "RUN único del piloto a buscar", required = true, example = "12345678-9")
        @PathVariable String run
    ) {
        try {
            Piloto piloto = pilotosService.getPilotoByRun(run);
            return ResponseEntity.ok(piloto);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Piloto no encontrado");
            error.put("mensaje", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @Operation(summary = "Obtener pilotos por Certificado", description = "Busca una lista de pilotos por su número de certificado DGAC")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Piloto encontrado por Certificado DGAC",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Piloto.class))),
        @ApiResponse(responseCode = "404", description = "No se encontró ningún piloto con el certificado DGAC proporcionado", content = @Content)
    })
    @GetMapping("/certificado/{certificado}")
    public ResponseEntity<Map<String, Object>> obtenerPorCertificado(
        @Parameter(description = "Número de certificado de la DGAC a buscar", required = true, example = "DGAC-123456")
        @PathVariable String certificado
    ) {
        List<Piloto> pilotosEncontrados = pilotosService.buscarPorCertificado(certificado);
        Map<String, Object> response = new HashMap<>();

        if (pilotosEncontrados == null || pilotosEncontrados.isEmpty()) {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("problema", "No se encontró ningún piloto con el certificado DGAC: " + certificado);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        
        response.put("status", HttpStatus.OK.value());
        response.put("mensaje", "Piloto encontrado por Certificado DGAC");
        response.put("datos", pilotosEncontrados);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Obtener pilotos con certificado por vencer", description = "Retorna un listado de pilotos cuyo certificado de la DGAC está próximo a vencer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda finalizada de manera correcta",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Piloto.class))),
        @ApiResponse(responseCode = "404", description = "No existen pilotos con certificado próximo a vencer", content = @Content)
    })
    @GetMapping("/por-vencer")
    public ResponseEntity<?> getPilotosConCertificadoPorVencer() {
        List<Piloto> pilotosx = pilotosService.getPilotosConCertificadoPorVencer();

        if (pilotosx.isEmpty()) {
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", "No existen pilotos con certificado próximo a vencer");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }

        return ResponseEntity.ok(pilotosx);
    }

    private ResponseEntity<Map<String, Object>> manejarErrores(BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errores = new HashMap<>();
        
        for (FieldError error : result.getFieldErrors()) {
            errores.put(error.getField(), error.getDefaultMessage());
        }
        
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("problema", "Existen errores de validación en los datos del piloto");
        response.put("errores", errores);
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}