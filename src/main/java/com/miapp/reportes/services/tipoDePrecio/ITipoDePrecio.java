package com.miapp.reportes.services.tipoDePrecio;

import com.miapp.sistemasdistribuidos.dto.TipoDePrecioDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITipoDePrecio {
    TipoDePrecioDTO save(TipoDePrecioDTO tipoDePrecioDTO);
    TipoDePrecioDTO findOne(Integer id);
    Page<TipoDePrecioDTO> findAll(Pageable pageable);
    void delete(Integer id);
    TipoDePrecioDTO update(TipoDePrecioDTO tipoDePrecioDTO);
}
