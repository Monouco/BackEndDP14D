package com.grupo4D.sag_system.repository;

import com.grupo4D.sag_system.model.Bloqueo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloqueoRepository extends CrudRepository<Bloqueo,Integer> {

}
