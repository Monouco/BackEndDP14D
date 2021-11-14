package com.grupo4D.sag_system.repository;

import com.grupo4D.sag_system.model.Bloqueo;
import com.grupo4D.sag_system.model.RutaXNodo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Repository
public interface BloqueoRepository extends CrudRepository<Bloqueo,Integer> {

    @Query(
            value = "SELECT * FROM bloqueo u WHERE u.activo = 1 " +
                    "and fecha_fin >= ?1 " +
                    "and fecha_inicio <= ?1",
            nativeQuery = true)
    public ArrayList<Bloqueo> listarBloqueosActuales(LocalDateTime fecha);

    @Query(
            value = "SELECT * FROM bloqueo u WHERE u.activo = 1 " +
                    "and ((fecha_fin >= ?2 and fecha_inicio <= ?1)" +
                    "or (fecha_fin <= ?2 and fecha_fin >= ?1)" +
                    "or (fecha_inicio <= ?2 and fecha_inicio >= ?1)) " +
                    "and tipo = ?3",
            nativeQuery = true)
    public ArrayList<Bloqueo> listarBloqueos24Horas(LocalDateTime fechaInicio, LocalDateTime fechaFin, int tipo);

}
