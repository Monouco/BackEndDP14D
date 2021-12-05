package com.grupo4D.sag_system.repository;

import com.grupo4D.sag_system.model.TipoCamion;
import com.grupo4D.sag_system.model.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario,Integer> {

    @Query(
            value = "select * from usuario u where nombre_usuario=?1 and activo =1 ",
            nativeQuery = true)
    public Usuario login(String nombreUsuario);

    public ArrayList<Usuario> findAllByActivoTrue();

}
