package com.grupo4D.sag_system.repository;

import com.grupo4D.sag_system.model.Averia;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AveriaRepository extends CrudRepository<Averia,Integer> {

}


