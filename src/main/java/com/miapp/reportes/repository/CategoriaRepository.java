package com.miapp.reportes.repository;

import com.miapp.sistemasdistribuidos.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    Page<Categoria> findByNombreCategoria(String nombreCategoria, Pageable pageable);
    Page<Categoria> findByNombreCategoriaContaining(String nombreCategoria, Pageable pageable);
}
