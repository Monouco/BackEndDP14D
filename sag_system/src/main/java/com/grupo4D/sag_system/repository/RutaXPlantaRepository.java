package com.grupo4D.sag_system.repository;

import com.grupo4D.sag_system.model.RutaXNodo;
import com.grupo4D.sag_system.model.RutaXPedido;
import com.grupo4D.sag_system.model.RutaXPlanta;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface RutaXPlantaRepository extends CrudRepository<RutaXPlanta,Integer> {


    @Query(
            value = "SELECT * FROM rutaxplanta u WHERE u.activo = 1 and id_ruta = ?1 order by secuencia asc",
            nativeQuery = true)
    public ArrayList<RutaXPlanta> listarRutaXPlantaPorRuta(int idRuta);

}
