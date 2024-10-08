package com.miapp.reportes.services.categoria;

import com.miapp.sistemasdistribuidos.entity.Categoria;
import com.miapp.sistemasdistribuidos.dto.CategoriaDTO;
import com.miapp.reportes.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoriaServiceImpl implements ICategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public CategoriaDTO save(CategoriaDTO dto) {
        Categoria domain = convertDtoToDomain(dto);
        Categoria savedCategoria = categoriaRepository.save(domain);
        return convertDomainToDto(savedCategoria);
    }

    @Override
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
    public boolean delete(Integer id) {
        if (categoriaRepository.existsById(id)) {
            categoriaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public CategoriaDTO getById(Integer id) {
        return categoriaRepository.findById(id)
                .map(this::convertDomainToDto)
                .orElse(null);
    }

    @Override
    public Page<CategoriaDTO> getAll(Pageable pageable) {
        Page<Categoria> categorias = categoriaRepository.findAll(pageable);
        return categorias.map(this::convertDomainToDto);
    }

    @Override
    public List<CategoriaDTO> findByNombre(String nombre) {
        List<Categoria> categorias = categoriaRepository.findByNombreCategoria(nombre);
        return categorias.stream().map(this::convertDomainToDto).collect(Collectors.toList());
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
