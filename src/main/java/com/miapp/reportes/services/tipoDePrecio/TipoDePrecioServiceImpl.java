package com.miapp.reportes.services.tipoDePrecio;

import com.miapp.sistemasdistribuidos.dto.TipoDePrecioDTO;
import com.miapp.sistemasdistribuidos.entity.TipoDePrecio;
import com.miapp.reportes.repository.TipoDePrecioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class TipoDePrecioServiceImpl implements ITipoDePrecio {

    @Autowired
    private TipoDePrecioRepository tipoDePrecioRepository;

    @Override
    public TipoDePrecioDTO save(TipoDePrecioDTO tipoDePrecioDTO) {
        TipoDePrecio tipo = convertDtoToEntity(tipoDePrecioDTO);
        TipoDePrecio savedTipo = tipoDePrecioRepository.save(tipo);
        return convertEntityToDto(savedTipo);
    }

    @Override
    public Page<TipoDePrecioDTO> findAll(Pageable pageable) {
        Page<TipoDePrecio> tipos = tipoDePrecioRepository.findAll(pageable);
        return tipos.map(this::convertEntityToDto);
    }

    @Override
    public TipoDePrecioDTO findOne(Integer id) {
        return tipoDePrecioRepository.findById(id)
                .map(this::convertEntityToDto)
                .orElse(null);
    }

    @Override
    public void delete(Integer id) {
        if (tipoDePrecioRepository.existsById(id)) {
            tipoDePrecioRepository.deleteById(id);
        } else {
            // Manejar el caso en que el estado no existe
            throw new RuntimeException("Estado no encontrado con id: " + id);
        }
    }

    @Override
    public TipoDePrecioDTO update(TipoDePrecioDTO tipoDePrecioDTO) {
        if (tipoDePrecioDTO.getTipoDePrecioId() == null) {
            throw new IllegalArgumentException("El ID del estado debe ser proporcionado para la actualización.");
        }
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
