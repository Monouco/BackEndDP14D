package com.grupo4D.sag_system.repository;

import com.grupo4D.sag_system.model.Nodo;
import com.grupo4D.sag_system.model.Pedido;
import com.grupo4D.sag_system.model.Planta;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Repository
public interface PlantaRepository extends CrudRepository<Planta,Integer> {

    @Query(
            value = "SELECT * FROM planta u WHERE u.activo = 1 " +
                    "ORDER BY tipo_planta asc" ,
            nativeQuery = true)
    public ArrayList<Planta> listarPlantas();

    @Transactional
    @Modifying
    @Query(value="call pr_fill_deposit( :cur_type)", nativeQuery = true)
    void fillDeposit( @Param("cur_type") int cur_type);


    @Query(
            value = "SELECT * FROM planta u WHERE u.activo = 1 and id_planta=?1 " ,
            nativeQuery = true)
    public Planta listarGLPRestanteXPlanta(int planta);

    @Query(
            value = "select * from planta where tipo_planta=1" ,
            nativeQuery = true)
    public Planta obtenerPlantaPrincipal();
}

