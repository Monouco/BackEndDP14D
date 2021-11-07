package com.grupo4D.sag_system.repository;

import com.grupo4D.sag_system.model.Averia;
import com.grupo4D.sag_system.model.Bloqueo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Repository
public interface AveriaRepository extends CrudRepository<Averia,Integer> {

    @Query(
            value = "SELECT u.* " +
                    "FROM averia u " +
                    "inner join camion c on c.id_camion = u.id_camion " +
                    "WHERE u.activo = 1 " +
                    "and c.activo = 1 " +
                    "and u.tipo = ?1 " +
                    "and ((c.estado = 'Averiado' " +
                    "and u.tipo = 1) " +
                    "or (c.estado_simulacion = 'Averiado' " +
                    "and u.tipo = 2) " +
                    "or (c.estado_colapso = 'Averiado' " +
                    "and u.tipo = 3))",
            nativeQuery = true)
    public ArrayList<Averia> listarAveriasActuales(int tipo);

}


