package com.grupo4D.sag_system.repository;

import com.grupo4D.sag_system.model.RutaXPedido;
import com.grupo4D.sag_system.model.RutaXPlanta;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RutaXPlantaRepository extends CrudRepository<RutaXPlanta,Integer> {

}
