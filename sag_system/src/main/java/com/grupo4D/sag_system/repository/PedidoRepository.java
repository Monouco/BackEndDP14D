package com.grupo4D.sag_system.repository;



import com.grupo4D.sag_system.model.NodoXBloqueo;
import com.grupo4D.sag_system.model.Pedido;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Repository
public interface PedidoRepository extends CrudRepository<Pedido,Integer> {

    public ArrayList<Pedido> findPedidosByEstadoPedido(String estado);


    @Query(
            value = "SELECT * FROM pedido u WHERE u.activo = 1 " +
                    "and estado_pedido = 'Nuevo' " +
                    "and fecha_pedido <= ?1 " +
                    "and tipo = ?2 ",
            nativeQuery = true)
    public ArrayList<Pedido> listarPedidosDisponibles(LocalDateTime fechaActual, int tipo);

}
