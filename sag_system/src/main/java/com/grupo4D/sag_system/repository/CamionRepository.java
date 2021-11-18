package com.grupo4D.sag_system.repository;

import com.grupo4D.sag_system.model.Camion;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;

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


}
