package com.grupo4D.sag_system.repository;

import com.grupo4D.sag_system.model.Ruta;
import com.grupo4D.sag_system.model.RutaXPedido;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface RutaXPedidoRepository extends CrudRepository<RutaXPedido,Integer> {



    public ArrayList<RutaXPedido> findRutaXPedidosByIdRuta(int idRuta);

}

