package com.grupo4D.sag_system.repository;

import com.grupo4D.sag_system.model.Ruta;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface RutaRepository extends CrudRepository<Ruta,Integer> {
    @Query(
            value = "SELECT * FROM ruta u WHERE u.estado = ?1 " +
                    "and u.activo = 1 " +
                    "and u.tipo = ?2",
            nativeQuery = true)
    public ArrayList<Ruta> listarRutasDisponibles(String estado, int tipo);
}
