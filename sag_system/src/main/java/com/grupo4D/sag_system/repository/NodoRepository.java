package com.grupo4D.sag_system.repository;

import com.grupo4D.sag_system.model.Nodo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NodoRepository extends CrudRepository<Nodo,Integer> {

    //@Query(value = "select u from Nodo u where u.coordenadaX = ?1 and u.coordenadaY = ?2", nativeQuery = true)
    //public Nodo findIdNodoByCoordenadaXAndCoordenadaY(int coordenadaX, double coordenadaY);
    public Nodo findIdNodoByCoordenadaXAndCoordenadaYAndActivoTrue(int coordenadaX, int coordenadaY);

    public Nodo findNodoById(int idNodo);
}

