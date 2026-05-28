package cl.GestionDrones.v1.pilotos.service;
import cl.GestionDrones.v1.pilotos.repository.PilotosRepository;
import java.util.List;

import cl.GestionDrones.v1.pilotos.dto.UpdatePilotoRequest;
import cl.GestionDrones.v1.pilotos.model.Piloto;
import org.springframework.stereotype.Service;

@Service
public class PilotosService {

    private final PilotosRepository pilotosRepository;

    // Inyección de dependencias por constructor
    public PilotosService(PilotosRepository pilotosRepository) {
        this.pilotosRepository = pilotosRepository;
    }

    // 1. Obtener todos los pilotos
    public List<Piloto> getAllPilotos() {
        return pilotosRepository.findAll();
    }

    // 2. Obtener por ID (Retorna null si no existe para que lo maneje el Controller)
    public Piloto getPilotoById(Integer id) {
        return pilotosRepository.findById(id).orElse(null);
    }

    // 3. Crear piloto
    public Piloto createPiloto(Piloto piloto) {
        return pilotosRepository.save(piloto);
    }

    // 4. Actualizar piloto (Usando la sintaxis del record)
    public Piloto updatePiloto(Integer id, UpdatePilotoRequest request) {
        Piloto pilotoExistente = getPilotoById(id);
        
        if (pilotoExistente == null) {
            return null;
        }

        // Mapeo usando los métodos del record y los setters de tu Entidad
        pilotoExistente.setRun(request.run());
        pilotoExistente.setNombres(request.nombre());
        pilotoExistente.setApellidos(request.apellido());
        pilotoExistente.setCorreo(request.correo());
        pilotoExistente.setTelefono(request.telefono());
        pilotoExistente.setNumeroCertificadoDgac(request.numeroCertificadoDgac());
        pilotoExistente.setFechaVencimientoCertificacion(request.fechaExpiracionCertificado()); 

        return pilotosRepository.save(pilotoExistente);
    }

    // 5. Eliminar piloto (Retorna boolean para la validación con if del Controller)
    public boolean deletePiloto(Integer id) {
        if (!pilotosRepository.existsById(id)) {
            return false;
        }
        pilotosRepository.deleteById(id);
        return true;
    }

    // 6. Buscar por RUN (Ajustado exactamente a 'int run' como tu repositorio)
    public List<Piloto> buscarPorRun(int run) {
        return pilotosRepository.buscarPorRun(run);
    }

    // 7. Buscar por Certificado DGAC
    public List<Piloto> buscarPorCertificado(String certificado) {
        return pilotosRepository.buscarPorCertificado(certificado);
    }
}