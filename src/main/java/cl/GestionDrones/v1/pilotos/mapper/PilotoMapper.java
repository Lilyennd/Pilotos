package cl.GestionDrones.v1.pilotos.mapper;

import cl.GestionDrones.v1.pilotos.dto.CreatePilotoRequest;
import cl.GestionDrones.v1.pilotos.dto.UpdatePilotoRquest;
import cl.GestionDrones.v1.pilotos.model.Piloto;

public class PilotoMapper {
    public static Piloto toPiloto(CreatePilotoRequest request) {
        return new Piloto(
            0, 
            request.run(),
            request.nombre(),
            request.apellido(),
            request.correo(), // <- use existing getter
            request.telefono(),
            request.numeroCertificadoDgac(),
            request.fechaExpiracionCertificado()
        );
    }

    public static Piloto toPiloto(UpdatePilotoRquest request) {
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