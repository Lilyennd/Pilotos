package cl.GestionDrones.v1.pilotos.service;
import cl.GestionDrones.v1.pilotos.repository.PilotosRepository;

import java.time.LocalDate;

import java.time.LocalDate;
import java.util.List;

import cl.GestionDrones.v1.pilotos.dto.CreatePilotoRequest;
import cl.GestionDrones.v1.pilotos.dto.UpdatePilotoRequest;
import cl.GestionDrones.v1.pilotos.exception.CertificadoVencidoException;
import cl.GestionDrones.v1.pilotos.exception.ResourceNotFoundException;
import cl.GestionDrones.v1.pilotos.mapper.PilotoMapper;
import cl.GestionDrones.v1.pilotos.model.Piloto;
import org.springframework.stereotype.Service;

@Service
public class PilotosService {

    private final PilotosRepository pilotosRepository;


    public PilotosService(PilotosRepository pilotosRepository) {
        this.pilotosRepository = pilotosRepository;
    }


    public List<Piloto> getAllPilotos() {
        return pilotosRepository.findAll();
    }


    public Piloto getPilotoById(Integer id) {
        return pilotosRepository.findById(id).orElse(null);
    }


    public Piloto savePiloto(CreatePilotoRequest request) {
        if (request.fechaVencimientoCertificacion() != null && 
            request.fechaVencimientoCertificacion().isBefore(LocalDate.now())) {
            
            throw new CertificadoVencidoException(
                "El certificado DGAC ya se encuentra vencido.", 
                request.numeroCertificadoDgac(), 
                request.fechaVencimientoCertificacion()
            );
        }

        Piloto nuevoPiloto = PilotoMapper.toEntity(request);
        return pilotosRepository.save(nuevoPiloto);
    }



    public Piloto updatePiloto(Integer id, UpdatePilotoRequest request) {
        Piloto pilotoExistente = getPilotoById(id);
        
        if (pilotoExistente == null) {
            return null;
        }

     
        pilotoExistente.setRun(request.run());
        pilotoExistente.setNombres(request.nombres());
        pilotoExistente.setApellidos(request.apellidos());
        pilotoExistente.setTelefono(request.telefono());
        pilotoExistente.setNumeroCertificadoDgac(request.numeroCertificadoDgac());
        pilotoExistente.setFechaVencimientoCertificacion(request.fechaVencimientoCertificacion ()); 

        return pilotosRepository.save(pilotoExistente);
    }

  
    public boolean deletePiloto(Integer id) {
        if (!pilotosRepository.existsById(id)) {
            return false;
        }
        pilotosRepository.deleteById(id);
        return true;
    }

    
    public Piloto getPilotoByRun(String run) {
        return pilotosRepository.findByRun(run)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "No se encontró un piloto registrado con el RUN: " + run));
    }

    public List<Piloto> buscarPorCertificado(String certificado) {
        return pilotosRepository.buscarPorCertificado(certificado);
    }



    public List<Piloto> getPilotosConCertificadoPorVencer() {

    LocalDate hoy = LocalDate.now();
    LocalDate fechaLimite = hoy.plusDays(30);

    return pilotosRepository
            .findAll()
            .stream()
            .filter(a -> !a.getFechaVencimientoCertificacion().isBefore(hoy))
            .filter(a -> !a.getFechaVencimientoCertificacion().isAfter(fechaLimite))
            .toList();
    }
}