package com.grupo4D.sag_system.repository;

import com.grupo4D.sag_system.model.Camion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CamionRepository extends CrudRepository<Camion,Integer> {

}
