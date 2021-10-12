package com.grupo4D.sag_system.repository;

import com.grupo4D.sag_system.model.Rol;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends CrudRepository<Rol,Integer> {

}

