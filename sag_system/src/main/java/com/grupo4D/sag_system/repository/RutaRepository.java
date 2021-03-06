package com.grupo4D.sag_system.repository;

import com.grupo4D.sag_system.model.Ruta;
import com.grupo4D.sag_system.model.RutaXPedido;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Repository
public interface RutaRepository extends CrudRepository<Ruta,Integer> {
    @Query(
            value = "SELECT * FROM ruta u WHERE u.estado_ruta = ?1 " +
                    "and u.activo = 1 " +
                    "and u.tipo = ?2 " +
                    "order by u.id_camion",
            nativeQuery = true)
    public ArrayList<Ruta> listarRutasDisponibles(String estado, int tipo);

    @Transactional
    @Modifying
    @Query(value="call pr_devolver_glp(:cur_seq, :cur_type, :cur_id_ruta)", nativeQuery = true)
    void devolverGLP(@Param("cur_seq") int cur_seq, @Param("cur_type") int cur_type, @Param("cur_id_ruta") int cur_id_ruta);


//    @Query(
//            value = "",
//            nativeQuery = true)
    public Ruta findRutaByIdAndActivoTrue(int id);

    @Query(
            value = "select * from ruta where fecha_fin like ?1 and activo=1 and tipo= ?2 and estado_ruta='Completado' order by fecha_fin",
            nativeQuery = true)
    public ArrayList<Ruta> rutasCompletadasPorFechaYTipo(String fecha, int tipo);

    @Query(
            value = "select * from ruta where  DATE(fecha_fin) >=?1 and DATE(fecha_fin) <=  ?2 " +
                    " and activo=1 and tipo= ?3 and estado_ruta='Completado' order by fecha_fin asc",
            nativeQuery = true)
    public ArrayList<Ruta> rutasCompletadasEntreFechasPorTipo(String fI, String fF, int tipo);



}
