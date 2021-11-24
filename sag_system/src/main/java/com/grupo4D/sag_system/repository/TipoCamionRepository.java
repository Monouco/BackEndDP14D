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

}
