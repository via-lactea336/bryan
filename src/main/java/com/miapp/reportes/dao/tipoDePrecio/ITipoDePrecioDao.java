package com.miapp.reportes.dao.tipoDePrecio;

import com.miapp.sistemasdistribuidos.entity.TipoDePrecio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITipoDePrecioDao extends JpaRepository<TipoDePrecio, Integer> {
    // obtener todos los tipos de precio con paginación
    Page<TipoDePrecio> findAll(Pageable pageable);
}
