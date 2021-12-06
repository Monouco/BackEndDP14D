package com.grupo4D.sag_system.repository;

import com.grupo4D.sag_system.model.Pedido;
import com.grupo4D.sag_system.model.TipoCamion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Repository
public interface TipoCamionRepository extends CrudRepository<TipoCamion,Integer> {


    @Query(
            value = "SELECT t.id_tipo_camion, abreviatura, capacidad_glp, capacidad_petroleo, peso_glp, peso_tara " +
                    " FROM tipo_camion t INNER JOIN camion c ON t.id_tipo_camion = c.id_tipo_camion  WHERE c.id_camion = ?1 " +
                    " and c.activo = 1   ",
            nativeQuery = true)
    public TipoCamion obtenerDatosTipoCamion(int id);

    @Query(
            value = "SELECT * from tipo_camion t where t.activo=1 and id_tipo_camion = ?1",
            nativeQuery = true)
    public TipoCamion listarTipoCamion(int id);

    public TipoCamion findTipoCamionById(int id);

    @Query(
            value = "with nPedidos as " +
                    "( " +
                    " select count(id_pedido) totalPedido, " +
                    "    ifnull(sum(cantidad_glp),1) totalGlp " +
                    "    from ( " +
                    "  select distinct p.id_pedido id_pedido, " +
                    "        p.cantidad_glp cantidad_glp " +
                    "  from  ruta r  " +
                    "  inner join rutaxpedido rp " +
                    "  on rp.id_ruta = r.id_ruta " +
                    "  inner join pedido p " +
                    "  on p.id_pedido = rp.id_pedido " +
                    "  where 1=1 " +
                    "  and r.activo = 1 " +
                    "  and rp.activo = 1 " +
                    "  and p.activo = 1 " +
                    "  and r.tipo = ?1 " +
                    "    ) pedidosProgramados " +
                    ") " +
                    "select t.id_tipo_camion, " +
                    "t.abreviatura, " +
                    "ifnull(( " +
                    " select sum(rp.cantidad_glp_enviado) " +
                    " from camion c   " +
                    " inner join ruta r on " +
                    " r.id_camion = c.id_camion " +
                    " inner join rutaxpedido rp " +
                    " on rp.id_ruta = r.id_ruta " +
                    " inner join pedido p " +
                    " on p.id_pedido = rp.id_pedido " +
                    "    where  " +
                    " t.id_tipo_camion = c.id_tipo_camion " +
                    "    and c.activo = 1 " +
                    "    and r.activo = 1 " +
                    "    and rp.activo = 1 " +
                    "    and p.activo = 1 " +
                    "    and r.tipo = ?1 " +
                    ") / ifnull(np.totalGlp,1),0) *100 porcentajeGLPAtendido, " +
                    "( " +
                    " ifnull(select count(distinct p.id_pedido) " +
                    " from camion c   " +
                    " inner join ruta r on " +
                    " r.id_camion = c.id_camion " +
                    " inner join rutaxpedido rp " +
                    " on rp.id_ruta = r.id_ruta " +
                    " inner join pedido p " +
                    " on p.id_pedido = rp.id_pedido " +
                    "    where  " +
                    " t.id_tipo_camion = c.id_tipo_camion " +
                    "    and c.activo = 1 " +
                    "    and r.activo = 1 " +
                    "    and rp.activo = 1 " +
                    "    and p.activo = 1 " +
                    "    and r.tipo = ?1 " +
                    ") / ifnull(np.totalPedido,1),0) * 100 porcentajePedidosAtendido " +
                    "from tipo_camion t, " +
                    "nPedidos np " +
                    "where t.activo = 1 " +
                    "order by id_tipo_camion asc",
            nativeQuery = true)
    public ArrayList<Object[]> porcentajeUsoTipo(int tipo);

}
