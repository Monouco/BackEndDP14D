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
                    "or (estado_colapso = ?1 and 3 = ?2))",
            nativeQuery = true)
    public ArrayList<Camion> listarCamionesTipo(String estado, int tipo);

    @Transactional
    @Modifying
    @Query(value="call pr_update_values(:cur_date, :cur_type)", nativeQuery = true)
    void updatingValues(@Param("cur_date") LocalDateTime cur_date, @Param("cur_type") int cur_type);

    public ArrayList<Camion> findCamionsByEstadoAndActivoTrue(String estado);


    @Query(
            value = "select concat(t.abreviatura , '-' , c.codigo_camion)"+
                    "from tipo_camion t inner join"+
                    "camion c on t.id_tipo_camion = c.id_tipo_camion" ,
                    nativeQuery = true)
    public ArrayList<String> listarCodigosCamion();


}
