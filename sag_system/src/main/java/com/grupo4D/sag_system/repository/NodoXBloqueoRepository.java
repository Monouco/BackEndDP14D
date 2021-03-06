package com.grupo4D.sag_system.repository;

import com.grupo4D.sag_system.model.Bloqueo;
import com.grupo4D.sag_system.model.NodoXBloqueo;
import com.grupo4D.sag_system.model.RutaXNodo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Repository
public interface NodoXBloqueoRepository extends CrudRepository<NodoXBloqueo,Integer> {

    @Query(
            value = "SELECT * FROM nodoxbloqueo u WHERE u.activo = 1 " +
                    "and id_bloqueo = ?1 ",
            nativeQuery = true)
    public ArrayList<NodoXBloqueo> listarNodosXBloqueo(int idBloqueo);

}
