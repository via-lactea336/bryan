package com.miapp.reportes.controller;

import com.miapp.reportes.services.estado.IEstadoService;
import com.miapp.sistemasdistribuidos.dto.EstadoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/estados")
public class EstadoController {

    @Autowired
    private IEstadoService estadoService;

    @PostMapping
    public ResponseEntity<EstadoDTO> createEstado(@RequestBody EstadoDTO estadoDTO) {
        EstadoDTO savedEstado = estadoService.save(estadoDTO);
        return ResponseEntity.ok(savedEstado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadoDTO> updateEstado(@PathVariable Integer id, @RequestBody EstadoDTO estadoDTO) {
        if (estadoDTO.getEstadoId() == null || !estadoDTO.getEstadoId().equals(id)) {
            return ResponseEntity.badRequest().build();
        }
        EstadoDTO updatedEstado = estadoService.update(estadoDTO);
        if (updatedEstado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedEstado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoDTO> getEstadoById(@PathVariable Integer id) {
        EstadoDTO estadoDTO = estadoService.findOne(id);
        if (estadoDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(estadoDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstado(@PathVariable Integer id) {
        try {
            estadoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<Page<EstadoDTO>> getAllEstados(Pageable pageable) {
        Page<EstadoDTO> estados = estadoService.findAll(pageable);
        return ResponseEntity.ok(estados);
    }
}
