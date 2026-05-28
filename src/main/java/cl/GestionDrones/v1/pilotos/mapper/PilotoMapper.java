package cl.GestionDrones.v1.pilotos.mapper;

import cl.GestionDrones.v1.pilotos.dto.UpdatePilotoRequest;
import cl.GestionDrones.v1.pilotos.dto.CreatePilotoRequest;
import cl.GestionDrones.v1.pilotos.model.Piloto;

public class PilotoMapper {

    public static Piloto toEntity(CreatePilotoRequest request) {
        Piloto piloto = new Piloto();
        
        piloto.setRun(request.run());
        piloto.setNombres(request.nombres()); 
        piloto.setApellidos(request.apellidos()); 
        piloto.setCorreo(request.correo());
        piloto.setTelefono(request.telefono());
        piloto.setNumeroCertificadoDgac(request.numeroCertificadoDgac());
        piloto.setFechaVencimientoCertificacion(request.fechaVencimientoCertificacion()); 
        
        return piloto;
    }
    

    public static Piloto toPiloto(UpdatePilotoRequest request) {
        return new Piloto(
            0, 
            request.run(),
            request.nombre(),
            request.apellido(),
            request.correo(), 
            request.telefono(),
            request.numeroCertificadoDgac(),
            request.fechaExpiracionCertificado()
        );
    }
}