package com.miapp.reportes.repository;

import com.miapp.sistemasdistribuidos.entity.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EstadoRepository extends JpaRepository<Estado, Integer>  {
    Page<Estado> findAll(Pageable pageable);
}
