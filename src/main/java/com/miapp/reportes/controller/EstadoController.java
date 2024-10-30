package com.miapp.reportes.controller;

import com.miapp.reportes.services.estado.IEstadoService;
import com.miapp.sistemasdistribuidos.dto.EstadoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/estados")
public class EstadoController {

    private static final Logger logger = LoggerFactory.getLogger(EstadoController.class);

    @Autowired
    private IEstadoService estadoService;

    @PostMapping
    public ResponseEntity<EstadoDTO> createEstado(@RequestBody EstadoDTO estadoDTO) {
        logger.info("Creating new estado: {}", estadoDTO);
        EstadoDTO savedEstado = estadoService.save(estadoDTO);
        logger.info("Estado created successfully with ID: {}", savedEstado.getEstadoId());
        return ResponseEntity.ok(savedEstado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadoDTO> updateEstado(@PathVariable Integer id, @RequestBody EstadoDTO estadoDTO) {
        logger.info("Updating estado with ID: {}", id);
        if (estadoDTO.getEstadoId() == null || !estadoDTO.getEstadoId().equals(id)) {
            logger.warn("Invalid ID provided in request: expected ID: {}, received ID: {}", id, estadoDTO.getEstadoId());
            return ResponseEntity.badRequest().build();
        }
        EstadoDTO updatedEstado = estadoService.update(estadoDTO);
        if (updatedEstado == null) {
            logger.warn("Estado with ID: {} not found for update", id);
            return ResponseEntity.notFound().build();
        }
        logger.info("Estado with ID: {} updated successfully", id);
        return ResponseEntity.ok(updatedEstado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoDTO> getEstadoById(@PathVariable Integer id) {
        logger.info("Fetching estado with ID: {}", id);
        EstadoDTO estadoDTO = estadoService.findOne(id);
        if (estadoDTO == null) {
            logger.warn("Estado with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        }
        logger.info("Estado with ID: {} found", id);
        return ResponseEntity.ok(estadoDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstado(@PathVariable Integer id) {
        logger.info("Deleting estado with ID: {}", id);
        try {
            estadoService.delete(id);
            logger.info("Estado with ID: {} deleted successfully", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            logger.error("Failed to delete estado with ID: {}. Error: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<Page<EstadoDTO>> getAllEstados(Pageable pageable) {
        logger.info("Fetching all estados with pagination");
        Page<EstadoDTO> estados = estadoService.findAll(pageable);
        return ResponseEntity.ok(estados);
    }
}
