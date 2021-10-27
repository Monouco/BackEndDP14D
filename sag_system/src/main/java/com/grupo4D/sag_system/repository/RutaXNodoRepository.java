package com.grupo4D.sag_system.repository;

import com.grupo4D.sag_system.model.RutaXNodo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface RutaXNodoRepository extends CrudRepository<RutaXNodo,Integer> {
    public ArrayList<RutaXNodo> findRutaXNodosByIdAndActivoTrueOrderBySecuenciaAsc(int idRuta);

    @Query(
            value = "SELECT * FROM rutaxnodo u WHERE u.activo = 1 " +
                    "and id_ruta = ?1 " +
                    "order by secuencia asc",
            nativeQuery = true)
    public ArrayList<RutaXNodo> listarRutaXNodosPorRuta(int idRuta);
}

