package com.grupo4D.sag_system.repository;

import com.grupo4D.sag_system.model.Camion;
import com.grupo4D.sag_system.model.reports.ReporteCamionConsumoMensual;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface CamionRepository extends CrudRepository<Camion,Integer> {


    @Query(
            value = "SELECT * FROM camion u WHERE u.activo = 1 " +
                    "and ((estado = ?1 and 1 = ?2) " +
                    "or (estado_simulacion = ?1 and 2 = ?2) " +
                    "or (estado_colapso = ?1 and 3 = ?2))" +
                    "order by id_tipo_camion desc",
            nativeQuery = true)
    public ArrayList<Camion> listarCamionesTipo(String estado, int tipo);

    @Query(
            value = "SELECT * FROM camion u WHERE u.activo = 1 " +
                    "and ((estado = ?1 and 1 = ?2) " +
                    "or (estado_simulacion = ?1 and 2 = ?2) " +
                    "or (estado_colapso = ?1 and 3 = ?2))" +
                    "order by id_tipo_camion asc",
            nativeQuery = true)
    public ArrayList<Camion> listarCamionesTipoAsc(String estado, int tipo);

    @Transactional
    @Modifying
    @Query(value="call pr_update_values(:cur_date, :cur_type, :cur_offset)", nativeQuery = true)
    void updatingValues(@Param("cur_date") LocalDateTime cur_date, @Param("cur_type") int cur_type, @Param("cur_offset") long cur_offset);


    public ArrayList<Camion> findCamionsByActivoTrue();

    public ArrayList<Camion> findCamionsByEstadoAndActivoTrue(String estado);

    @Transactional
    @Modifying
    @Query(
            value = "UPDATE camion  SET estado = ?1    WHERE id_camion = ?2" ,
            nativeQuery = true)
    public void cambiarEstadoCamion(String estado, int id);


    @Query(
            value = "select concat(t.abreviatura , '-' , c.codigo_camion) "+
                    "from tipo_camion t inner join "+
                    "camion c on t.id_tipo_camion = c.id_tipo_camion " ,
                    nativeQuery = true)
    public ArrayList<String> listarCodigosCamion();

    @Query(
            value = "select concat(t.abreviatura , '-' , c.codigo_camion) "+
                    "from tipo_camion t inner join "+
                    "camion c on t.id_tipo_camion = c.id_tipo_camion "+
                    "where c.id_camion = ?1 ",
            nativeQuery = true)
    public String listarCodigo1Camion(int id);

    public Camion findCamionById(int id);

    @Query(
            value = "select count(*) "+
                    "from camion  ",
            nativeQuery = true)
    public int tamanoFlota();

    @Query(
            value = "select id_camion "+
                    "from camion " +
                    "where activo = 1 " +
                    "order by id_camion asc",
            nativeQuery = true)
    public ArrayList<Integer> obtenerIds();

    @Query(
            value = "select c.* "+
                    "from camion c " +
                    "inner join tipo_camion t " +
                    "on c.id_tipo_camion = t.id_tipo_camion " +
                    "where c.activo = 1 " +
                    "and t.activo = 1 " +
                    "and c.codigo_camion = ?2 " +
                    "and t.abreviatura = ?1" ,
            nativeQuery = true)
    public Camion obtenerCamion(String tipo, String codigo);

    @Query(
            value = "select  " +
                    "c.id_camion, " +
                    "concat(t.abreviatura , '-' , c.codigo_camion) codigo, " +
                    "sum(r.costo_operacion) petroleoConsumido, " +
                    "sum(ifnull(( " +
                    " select count(rn.secuencia) " +
                    "    from rutaxnodo rn " +
                    "    where rn.id_ruta = r.id_ruta " +
                    "    and rn.activo = 1 " +
                    "),0))  distanciaRecorrida, " +
                    "monthname(r.fecha_inicio) mes, " +
                    "year(r.fecha_inicio) agno " +
                    "from " +
                    "camion c  " +
                    "inner join ruta r  " +
                    "on r.id_camion = c.id_camion " +
                    "inner join tipo_camion t " +
                    "on t.id_tipo_camion = c.id_tipo_camion " +
                    "where c.activo = 1 " +
                    "and r.activo = 1 " +
                    "and t.activo = 1 " +
                    "and r.tipo = 1 " +
                    "and (r.fecha_inicio between ?1 and ?2) " +
                    //"or r.fecha_fin between ?1 and ?2) " +
                    "group by c.id_camion, " +
                    "concat(t.abreviatura , '-' , c.codigo_camion) , " +
                    "monthname(r.fecha_inicio)," +
                    "year(r.fecha_inicio) " +
                    "order by " +
                    "year(r.fecha_inicio) desc" ,
            nativeQuery = true
    )
    public List<Object[]> generarReporteConsumoMensual(LocalDateTime inicio, LocalDateTime fin, int tipo);
}
