package cl.GestionDrones.v1.pilotos.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.GestionDrones.v1.pilotos.model.Piloto;
import cl.GestionDrones.v1.pilotos.service.PilotosService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/api/v1/pilotos")

public class PilotoController {

    private final PilotosService pilotosService;
    public PilotoController(PilotosService pilotosService) {
        this.pilotosService = pilotosService;
    }
    
    @GetMapping
    public ResponseEntity<List<Piloto>> getAllPilotos() {
        List<Piloto> pilotos = pilotosService.getAllPilotos();
        return ResponseEntity.ok(pilotos);
    }

    @GetMapping("/run")
    public ResponseEntity<List<Piloto>> buscarPilotos(@RequestParam int run) {
        List<Piloto> pilotos = pilotosService.buscarPorRun(run);
        return ResponseEntity.ok(pilotos);
    }

    @GetMapping("/certificado")
    public ResponseEntity<List<Piloto>> buscarPilotosPorCertificado(@RequestParam String certificado) {
        List<Piloto> pilotos = pilotosService.buscarPorCertificado(certificado);
        return ResponseEntity.ok(pilotos);
    }

    @PostMapping
    public ResponseEntity<Piloto> createPiloto(@RequestBody Piloto piloto) {
        Piloto nuevoPiloto = pilotosService.createPiloto(piloto);
        return ResponseEntity.status(201).body(nuevoPiloto);
    }
    
}
