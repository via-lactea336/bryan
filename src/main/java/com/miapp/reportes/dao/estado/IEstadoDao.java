package com.miapp.reportes.dao.estado;

import com.miapp.sistemasdistribuidos.entity.Estado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEstadoDao extends JpaRepository<Estado, Integer> {
    // obtener todos los estados con paginación
    Page<Estado> findAll(Pageable pageable);
}
