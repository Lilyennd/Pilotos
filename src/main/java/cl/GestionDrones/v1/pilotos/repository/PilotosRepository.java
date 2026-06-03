package cl.GestionDrones.v1.pilotos.repository;


import cl.GestionDrones.v1.pilotos.model.Piloto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository

public interface  PilotosRepository extends JpaRepository <Piloto , Integer> {

    @Query(value = "SELECT * FROM pilotos WHERE run = :run", nativeQuery = true)
    List<Piloto> buscarPorRun(@Param("run") String run);

    @Query(value = "SELECT * FROM pilotos WHERE numero_certificado_dgac = :certificado", nativeQuery = true)
    List<Piloto> buscarPorCertificado(@Param("certificado") String certificado);

}

