package com.miapp.reportes.services.categoria;

import com.miapp.sistemasdistribuidos.entity.Categoria;
import com.miapp.sistemasdistribuidos.dto.CategoriaDTO;
import com.miapp.reportes.repository.CategoriaRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;


import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class CategoriaServiceImpl implements ICategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${longTimeoutDays}")  // 1 día
    private long longTimeoutDays;

    private static final String CACHE_KEY_PREFIX = "Categoria::api_categoria_";

    @Override
    @CachePut(value = "Categoria", key = "'api_categoria_' + #result.categoriaId")
    public CategoriaDTO save(CategoriaDTO dto) {
        Categoria domain = convertDtoToDomain(dto);
        Categoria savedCategoria = categoriaRepository.save(domain);
        return convertDomainToDto(savedCategoria);
    }

    @Override
    @CachePut(value = "Categoria", key = "'api_categoria_' + #id")
    public CategoriaDTO update(Integer id, CategoriaDTO dto) {
        if (dto.getCategoriaId() != null && !dto.getCategoriaId().equals(id)) {
            return null; // ID en el DTO debe coincidir con el ID en la URL
        }
        Categoria domain = convertDtoToDomain(dto);
        domain.setCategoriaId(id); // Asegúrate de que el ID sea el proporcionado
        Categoria updatedCategoria = categoriaRepository.save(domain);
        return convertDomainToDto(updatedCategoria);
    }

    @Override
    @CacheEvict(value = "Categoria", key = "'api_categoria_' + #id")
    public boolean delete(Integer id) {
        if (categoriaRepository.existsById(id)) {
            categoriaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    @Cacheable(value = "Categoria", key = "'api_categoria_' + #id")
    public CategoriaDTO getById(Integer id) {
        return categoriaRepository.findById(id)
                .map(this::convertDomainToDto)
                .orElse(null);
    }

    @Override
    public Page<CategoriaDTO> getAll(Pageable pageable) {
        Page<Categoria> categorias = categoriaRepository.findAll(pageable);
        categorias.forEach(categoria -> {
            // Cachear cada categoría individualmente
            redisTemplate.opsForValue().set(CACHE_KEY_PREFIX + categoria.getCategoriaId(),
                    convertDomainToDto(categoria), longTimeoutDays, TimeUnit.DAYS);
        });
        return categorias.map(this::convertDomainToDto);
    }

    @Override
    public Page<CategoriaDTO> findByNombre(String nombre, Pageable pageable) {
        Page<Categoria> categorias = categoriaRepository.findByNombreCategoria(nombre, pageable);
        return categorias.map(this::convertDomainToDto);
    }

    @Override
    public Page<CategoriaDTO> findByNombreContaining(String nombre, Pageable pageable) {
        Page<Categoria> categorias = categoriaRepository.findByNombreCategoriaContaining(nombre, pageable);
        return categorias.map(this::convertDomainToDto);
    }

    private CategoriaDTO convertDomainToDto(Categoria domain) {
        if (domain == null) {
            return null;
        }
        return new CategoriaDTO(domain.getCategoriaId(), domain.getNombreCategoria(), domain.getDescripcion());
    }

    private Categoria convertDtoToDomain(CategoriaDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Categoria(dto.getCategoriaId(), dto.getNombreCategoria(), dto.getDescripcion());
    }
}
