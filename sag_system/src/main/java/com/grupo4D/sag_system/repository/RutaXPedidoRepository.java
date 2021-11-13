package com.grupo4D.sag_system.repository;

import com.grupo4D.sag_system.model.Ruta;
import com.grupo4D.sag_system.model.RutaXPedido;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface RutaXPedidoRepository extends CrudRepository<RutaXPedido,Integer> {



    @Query(value = "select * from rutaxpedido where id_ruta = ?1", nativeQuery = true)
    public ArrayList<RutaXPedido> findRutaXPedidosByRuta(int idRuta);

    @Query(value = "select * from rutaxpedido " +
                   "where id_pedido = ?1 " +
                   "and activo = 1",
            nativeQuery = true)
    public ArrayList<RutaXPedido> listarRutaXPedido(Integer idPedido);
}

