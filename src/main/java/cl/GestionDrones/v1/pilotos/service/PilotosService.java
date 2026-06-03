package cl.GestionDrones.v1.pilotos.service;
import cl.GestionDrones.v1.pilotos.repository.PilotosRepository;
import java.util.List;

import cl.GestionDrones.v1.pilotos.dto.UpdatePilotoRequest;
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


    public Piloto createPiloto(Piloto piloto) {
        return pilotosRepository.save(piloto);
    }

    public Piloto updatePiloto(Integer id, UpdatePilotoRequest request) {
        Piloto pilotoExistente = getPilotoById(id);
        
        if (pilotoExistente == null) {
            return null;
        }

     
        pilotoExistente.setRun(request.run());
        pilotoExistente.setNombres(request.nombre());
        pilotoExistente.setApellidos(request.apellido());
        pilotoExistente.setTelefono(request.telefono());
        pilotoExistente.setNumeroCertificadoDgac(request.numeroCertificadoDgac());
        pilotoExistente.setFechaVencimientoCertificacion(request.fechaExpiracionCertificado()); 

        return pilotosRepository.save(pilotoExistente);
    }

  
    public boolean deletePiloto(Integer id) {
        if (!pilotosRepository.existsById(id)) {
            return false;
        }
        pilotosRepository.deleteById(id);
        return true;
    }

    
    public List<Piloto> buscarPorRun(String run) {
        return pilotosRepository.buscarPorRun(run);
    }

    public List<Piloto> buscarPorCertificado(String certificado) {
        return pilotosRepository.buscarPorCertificado(certificado);
    }
}