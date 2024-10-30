package com.miapp.reportes.services.tipoDePrecio;

import com.miapp.reportes.controller.TipoDePrecioController;
import com.miapp.sistemasdistribuidos.dto.TipoDePrecioDTO;
import com.miapp.sistemasdistribuidos.entity.TipoDePrecio;
import com.miapp.reportes.repository.TipoDePrecioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;


@Service
@Transactional
public class TipoDePrecioServiceImpl implements ITipoDePrecio {

    private static final Logger logger = LoggerFactory.getLogger(TipoDePrecioController.class);

    @Autowired
    private TipoDePrecioRepository tipoDePrecioRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${defaultTimeoutHours}") // 1 hora
    private long defaultTimeoutHours;

    private static final String CACHE_KEY_PREFIX = "TipoDePrecio::api_tipoDePrecio_";

    @Override
    @CachePut(value = "TipoDePrecio", key = "'api_tipoDePrecio_' + #result.tipoDePrecioId")
    public TipoDePrecioDTO save(TipoDePrecioDTO tipoDePrecioDTO) {
        logger.info("Guardando TipoDePrecio en la base de datos y actualizando cache.");
        TipoDePrecio tipo = convertDtoToEntity(tipoDePrecioDTO);
        TipoDePrecio savedTipo = tipoDePrecioRepository.save(tipo);
        return convertEntityToDto(savedTipo);
    }

    @Override
    public Page<TipoDePrecioDTO> findAll(Pageable pageable) {
        // Aquí no caches la paginación completa
        Page<TipoDePrecio> tipos = tipoDePrecioRepository.findAll(pageable);
        tipos.forEach(tipo -> {
            // Cachear cada entidad individualmente
            logger.info("Buscando TipoDePrecio con id {} en cache o base de datos.", tipo.getTipoDePrecioId());
            redisTemplate.opsForValue().set(CACHE_KEY_PREFIX + tipo.getTipoDePrecioId(),
                    convertEntityToDto(tipo),defaultTimeoutHours, TimeUnit.HOURS);
        });
        return tipos.map(this::convertEntityToDto);
    }

    @Override
    @Cacheable(value = "TipoDePrecio", key = "'api_tipoDePrecio_' + #id")
    public TipoDePrecioDTO findOne(Integer id) {
        logger.info("Buscando TipoDePrecio con id {} en cache o base de datos.", id);
        return tipoDePrecioRepository.findById(id)
                .map(this::convertEntityToDto)
                .orElse(null);
    }


    @Override
    @CacheEvict(value = "TipoDePrecio", key = "'api_tipoDePrecio_' + #id")
    public void delete(Integer id) {
        if (tipoDePrecioRepository.existsById(id)) {
            logger.info("Eliminando TipoDePrecio con id {} de la base de datos y cache.", id);
            tipoDePrecioRepository.deleteById(id);
        } else {
            throw new RuntimeException("TipoDePrecio no encontrado con id: " + id);
        }
    }

    @Override
    @CachePut(value = "TipoDePrecio", key = "'api_tipoDePrecio_' + #tipoDePrecioDTO.tipoDePrecioId")
    public TipoDePrecioDTO update(TipoDePrecioDTO tipoDePrecioDTO) {
        if (tipoDePrecioDTO.getTipoDePrecioId() == null) {
            throw new IllegalArgumentException("El ID del TipoDePrecio debe ser proporcionado para la actualización.");
        }
        logger.info("Actualizando TipoDePrecio con id {} en la base de datos y cache.", tipoDePrecioDTO.getTipoDePrecioId());
        TipoDePrecio tipo = convertDtoToEntity(tipoDePrecioDTO);
        TipoDePrecio updatedTipo = tipoDePrecioRepository.save(tipo);
        return convertEntityToDto(updatedTipo);
    }

    private TipoDePrecioDTO convertEntityToDto(TipoDePrecio tipoDePrecio) {
        if (tipoDePrecio == null) {
            return null;
        }
        return new TipoDePrecioDTO(tipoDePrecio.getTipoDePrecioId(), tipoDePrecio.getNombreTipo());
    }

    private TipoDePrecio convertDtoToEntity(TipoDePrecioDTO tipoDePrecio) {
        if (tipoDePrecio == null) {
            return null;
        }
        return new TipoDePrecio(tipoDePrecio.getTipoDePrecioId(), tipoDePrecio.getNombreTipo());
    }

}
