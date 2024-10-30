package com.miapp.reportes.services.categoria;

import com.miapp.sistemasdistribuidos.dto.CategoriaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICategoriaService {

    CategoriaDTO save(CategoriaDTO dto);

    CategoriaDTO getById(Integer id);

    Page<CategoriaDTO> getAll(Pageable pageable);

    Page<CategoriaDTO> findByNombre(String nombreCategoria, Pageable pageable);

    Page<CategoriaDTO> findByNombreContaining(String nombreCategoria, Pageable pageable);
    CategoriaDTO update(Integer id, CategoriaDTO categoriaDTO);
    boolean delete(Integer id);
}
