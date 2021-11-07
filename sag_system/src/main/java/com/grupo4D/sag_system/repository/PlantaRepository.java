package com.grupo4D.sag_system.repository;

import com.grupo4D.sag_system.model.Pedido;
import com.grupo4D.sag_system.model.Planta;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Repository
public interface PlantaRepository extends CrudRepository<Planta,Integer> {

    @Query(
            value = "SELECT * FROM planta u WHERE u.activo = 1 " +
                    "ORDER BY id_planta asc" ,
            nativeQuery = true)
    public ArrayList<Planta> listarPlantas();
}

