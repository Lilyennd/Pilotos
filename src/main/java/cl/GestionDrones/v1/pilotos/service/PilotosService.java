package cl.GestionDrones.v1.pilotos.service;
import cl.GestionDrones.v1.pilotos.repository.PilotosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import cl.GestionDrones.v1.pilotos.model.Piloto;
import org.springframework.stereotype.Service;

@Service
public class PilotosService {

    @Autowired
    private PilotosRepository pilotosRepository;

    public List<Piloto> getAllPilotos() {
        return pilotosRepository.findAll();
    }

    public Piloto getPilotoById(Integer id) {
        return pilotosRepository.findById(id).orElse(null);
    }

    public Piloto createPiloto(Piloto piloto) {
        return pilotosRepository.save(piloto);
    }
    public Piloto updatePiloto (Piloto piloto) {
        return pilotosRepository.save(piloto);
    }

    public String deletePiloto(Integer id) {
        pilotosRepository.deleteById(id);
        return "Piloto eliminado con éxito";
    }
    
    public List<Piloto> buscarPorRun(int run) {
        return pilotosRepository.buscarPorRun(run);
    }

    public List<Piloto> buscarPorCertificado(String certificado) {
        return pilotosRepository.buscarPorCertificado(certificado);
    }

}