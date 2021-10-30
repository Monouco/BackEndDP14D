package com.grupo4D.sag_system.repository;

import com.grupo4D.sag_system.model.Camion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface CamionRepository extends CrudRepository<Camion,Integer> {

    public ArrayList<Camion> findCamionsByEstadoAndActivoTrue(String estado);

}
