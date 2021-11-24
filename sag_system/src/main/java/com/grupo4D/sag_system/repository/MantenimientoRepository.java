package com.grupo4D.sag_system.repository;

import com.grupo4D.sag_system.model.Bloqueo;
import com.grupo4D.sag_system.model.Mantenimiento;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Repository
public interface MantenimientoRepository extends CrudRepository<Mantenimiento,Integer> {

    @Query(
            value = "SELECT * FROM mantenimiento u WHERE u.activo = 1 " +
                    "and tipo_simulacion = 1 " ,
            nativeQuery = true)
    public ArrayList<Mantenimiento> listarMantenimientos();
}

