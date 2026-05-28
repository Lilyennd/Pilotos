package cl.GestionDrones.v1.pilotos.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.GestionDrones.v1.pilotos.dto.CreatePilotoRequest;
import cl.GestionDrones.v1.pilotos.dto.UpdatePilotoRequest;
import cl.GestionDrones.v1.pilotos.model.Piloto;
import cl.GestionDrones.v1.pilotos.service.PilotosService;
import jakarta.validation.Valid;

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


@RestController
@RequestMapping("/api/v1/pilotos")
public class PilotosController {

    private final PilotosService pilotosService;

    public PilotosController(PilotosService pilotosService) {
        this.pilotosService = pilotosService;
    }

    // 1. Crear (POST)
    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody CreatePilotoRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return manejarErrores(result);
        }

        Piloto pilotoParaCrear = new Piloto();
        org.springframework.beans.BeanUtils.copyProperties(request, pilotoParaCrear);
        Piloto nuevoPiloto = pilotosService.createPiloto(pilotoParaCrear);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.CREATED.value());
        response.put("mensaje", "Piloto registrado exitosamente");
        response.put("datos", nuevoPiloto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 2. Obtener todos (GET)
    @GetMapping
    public ResponseEntity<Map<String, Object>> listarTodos() {
        List<Piloto> pilotos = pilotosService.getAllPilotos();
        Map<String, Object> response = new HashMap<>();
        
        if (pilotos.isEmpty()) {
            response.put("status", HttpStatus.NO_CONTENT.value());
            response.put("mensaje", "No existen pilotos registrados en el sistema");
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        }
        
        response.put("status", HttpStatus.OK.value());
        response.put("mensaje", "Listado de pilotos obtenido correctamente");
        response.put("datos", pilotos);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 3. Obtener por RUN / ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerPorId(@PathVariable int id) {
        List<Piloto> pilotosEncontrados = pilotosService.buscarPorRun(id);
        Map<String, Object> response = new HashMap<>();

        if (pilotosEncontrados == null || pilotosEncontrados.isEmpty()) {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("problema", "No se encontró ningún piloto con el RUN/ID proporcionado");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Piloto piloto = pilotosEncontrados.get(0);
        response.put("status", HttpStatus.OK.value());
        response.put("mensaje", "Piloto encontrado");
        response.put("datos", piloto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

 // 4. Actualizar (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable Integer id, @Valid @RequestBody UpdatePilotoRequest request, BindingResult result) {
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

    // 5. Eliminar (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable int id) {
        List<Piloto> pilotosEncontrados = pilotosService.buscarPorRun(id);
        Map<String, Object> response = new HashMap<>();

        if (pilotosEncontrados == null || pilotosEncontrados.isEmpty()) {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("problema", "No se encontró ningún piloto con el RUN/ID proporcionado para eliminar");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        // Ahora sí llamamos al método de eliminar del servicio (asegúrate de tenerlo creado en tu PilotosService)
        pilotosService.deletePiloto(id); 
        
        response.put("status", HttpStatus.OK.value());
        response.put("mensaje", "Piloto eliminado del sistema correctamente");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 6. Buscar por Certificado DGAC (GET)
    @GetMapping("/certificado/{certificado}")
    public ResponseEntity<Map<String, Object>> obtenerPorCertificado(@PathVariable String certificado) {
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

    // Método privado para estructurar los errores del DTO
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
