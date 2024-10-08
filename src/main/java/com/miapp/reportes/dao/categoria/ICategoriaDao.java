package com.miapp.reportes.dao.categoria;

import com.miapp.sistemasdistribuidos.entity.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoriaDao extends JpaRepository<Categoria, Integer> {

    // obtener todas las categorías con paginación
    Page<Categoria> findAll(Pageable pageable);

    // encontrar todas las categorías que coincidan exactamente con el nombre proporcionado
    List<Categoria> findByNombreCategoria(String nombreCategoria);

    // buscar categorías cuyo nombre contenga una nombre específico, con soporte para paginación
    Page<Categoria> findByNombreCategoriaContaining(String nombreCategoria, Pageable pageable);
}
