package com.miapp.reportes.services.estado;

import com.miapp.sistemasdistribuidos.dto.EstadoDTO;
import com.miapp.sistemasdistribuidos.entity.Estado;
import com.miapp.reportes.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;

import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class EstadoServiceImpl implements IEstadoService {

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${timeoutMinutes}")  // 10 minutos
    private long timeoutMinutes;

    private static final String CACHE_KEY_PREFIX = "Estado::api_estado_";

    @Override
    @CachePut(value = "Estado", key = "'api_estado_' + #result.estadoId")
    public EstadoDTO save(EstadoDTO estadoDTO) {
        Estado estado = convertDtoToEntity(estadoDTO);
        Estado savedEstado = estadoRepository.save(estado);
        return convertEntityToDto(savedEstado);
    }

    @Override
    public Page<EstadoDTO> findAll(Pageable pageable) {
        Page<Estado> estados = estadoRepository.findAll(pageable);
        estados.forEach(estado -> {
            // Cachear cada entidad individualmente
            redisTemplate.opsForValue().set(CACHE_KEY_PREFIX + estado.getEstadoId(),
                    convertEntityToDto(estado), timeoutMinutes, TimeUnit.MINUTES);
        });
        return estados.map(this::convertEntityToDto);
    }

    @Override
    @Cacheable(value = "Estado", key = "'api_estado_' + #id")
    public EstadoDTO findOne(Integer id) {
        return estadoRepository.findById(id)
                .map(this::convertEntityToDto)
                .orElse(null);
    }

    @Override
    @CacheEvict(value = "Estado", key = "'api_estado_' + #id")
    public void delete(Integer id) {
        if (estadoRepository.existsById(id)) {
            estadoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Estado no encontrado con id: " + id);
        }
    }

    @Override
    @CachePut(value = "Estado", key = "'api_estado_' + #estadoDTO.estadoId")
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
