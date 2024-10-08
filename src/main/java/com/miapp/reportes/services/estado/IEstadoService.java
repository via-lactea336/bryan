package com.miapp.reportes.services.estado;

import com.miapp.sistemasdistribuidos.dto.EstadoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface IEstadoService {
    EstadoDTO save(EstadoDTO estadoDTO);
    Page<EstadoDTO> findAll(Pageable pageable);
    EstadoDTO findOne(Integer id);
    void delete(Integer id);
    EstadoDTO update(EstadoDTO estadoDTO);
}
