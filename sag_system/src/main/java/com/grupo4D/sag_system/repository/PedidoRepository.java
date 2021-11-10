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
                    "and (estado_pedido = 'Nuevo' " +
                    "or estado_pedido = 'Por Reprogramar') " +
                    "and fecha_pedido <= ?1 " +
                    "and tipo = ?2 " +
                    "and cantidad_glp <> glp_enviado ",
            nativeQuery = true)
    public ArrayList<Pedido> listarPedidosDisponibles(LocalDateTime fechaActual, int tipo);


    @Query(
            value = "SELECT * FROM pedido u WHERE u.activo = 1 " +
                    "and tipo = 1 ",
            nativeQuery = true)
    public ArrayList<Pedido> listarPedidos();


    //posible cambio por algo mas eficiente que no requiera entrar a BD por cada pedido
    public Pedido findPedidoByIdAndActivoTrue(int id);

}

/*"SELECT u.id_pedido, " +
                    "u.activo, " +
                    "u.cantidad_glp - SUM(nvl(r.cantidad_glp_enviado,0)) cantidad_glp, " +
                    "u.estado_pedido, " +
                    "u.fecha_entrega, " +
                    "u.fecha_limite, " +
                    "u.fecha_pedido, " +
                    "u.plazo_entrega, " +
                    "u.tipo, " +
                    "u.id_nodo " +
                    "FROM pedido u " +
                    "inner join rutaxpedido r on r.id_pedido = u.id_pedido " +
                    "WHERE u.activo = 1 " +
                    "and (u.estado_pedido = 'Nuevo' " +
                    "or u.estado_pedido = 'Por Reprogramar') " +
                    "and u.fecha_pedido <= ?1 " +
                    "and u.tipo = ?2 " +
                    "and r.activo = 1 " +
                    "group by u.id_pedido, " +
                    "u.activo, " +
                    "u.estado_pedido, " +
                    "u.fecha_entrega, " +
                    "u.fecha_limite, " +
                    "u.fecha_pedido, " +
                    "u.plazo_entrega, " +
                    "u.tipo, " +
                    "u.id_nodo"*/