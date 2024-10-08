package com.miapp.reportes.services.estado;

import com.miapp.sistemasdistribuidos.dto.EstadoDTO;
import com.miapp.sistemasdistribuidos.entity.Estado;
import com.miapp.reportes.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EstadoServiceImpl implements IEstadoService {

    @Autowired
    private EstadoRepository estadoRepository;

    @Override
    public EstadoDTO save(EstadoDTO estadoDTO) {
        Estado estado = convertDtoToEntity(estadoDTO);
        Estado savedEstado = estadoRepository.save(estado);
        return convertEntityToDto(savedEstado);
    }

    @Override
    public Page<EstadoDTO> findAll(Pageable pageable) {
        Page<Estado> estados = estadoRepository.findAll(pageable);
        return estados.map(this::convertEntityToDto);
    }

    @Override
    public EstadoDTO findOne(Integer id) {
        return estadoRepository.findById(id)
                .map(this::convertEntityToDto)
                .orElse(null);
    }

    @Override
    public void delete(Integer id) {
        if (estadoRepository.existsById(id)) {
            estadoRepository.deleteById(id);
        } else {
            // Manejar el caso en que el estado no existe
            throw new RuntimeException("Estado no encontrado con id: " + id);
        }
    }

    @Override
    public EstadoDTO update(EstadoDTO estadoDTO) {
        if (estadoDTO.getEstadoId() == null) {
            throw new IllegalArgumentException("El ID del estado debe ser proporcionado para la actualización.");
        }
        Estado estado = convertDtoToEntity(estadoDTO);
        Estado updatedEstado = estadoRepository.save(estado);
        return convertEntityToDto(updatedEstado);
    }

    private EstadoDTO convertEntityToDto(Estado estado) {
        if (estado == null) {
            return null;
        }
        return new EstadoDTO(estado.getEstadoId(), estado.getNombreEstado());
    }

    private Estado convertDtoToEntity(EstadoDTO estadoDTO) {
        if (estadoDTO == null) {
            return null;
        }
        return new Estado(estadoDTO.getEstadoId(), estadoDTO.getNombreEstado());
    }
}
