package com.miapp.reportes.repository;
import com.miapp.sistemasdistribuidos.entity.Categoria;
import com.miapp.sistemasdistribuidos.entity.TipoDePrecio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TipoDePrecioRepository extends JpaRepository<TipoDePrecio, Integer> {
    Page<TipoDePrecio> findAll(Pageable pageable);
}
