package com.miapp.reportes.controller;

import com.miapp.sistemasdistribuidos.dto.TipoDePrecioDTO;
import com.miapp.reportes.services.tipoDePrecio.ITipoDePrecio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tipos-de-precio")
public class TipoDePrecioController {

    private static final Logger logger = LoggerFactory.getLogger(TipoDePrecioController.class);

    @Autowired
    private ITipoDePrecio tipoDePrecioService;

    @PostMapping
    public ResponseEntity<TipoDePrecioDTO> createTipoDePrecio(@RequestBody TipoDePrecioDTO tipoDePrecioDTO) {
        logger.info("Creating new TipoDePrecio: {}", tipoDePrecioDTO);
        TipoDePrecioDTO savedTipoDePrecio = tipoDePrecioService.save(tipoDePrecioDTO);
        logger.info("TipoDePrecio created successfully with ID: {}", savedTipoDePrecio.getTipoDePrecioId());
        return ResponseEntity.ok(savedTipoDePrecio);
    }

    @GetMapping
    public ResponseEntity<Page<TipoDePrecioDTO>> getAllTipoDePrecios(Pageable pageable) {
        logger.info("Fetching all TiposDePrecio with pagination");
        Page<TipoDePrecioDTO> tiposDePrecio = tipoDePrecioService.findAll(pageable);
        logger.info("Fetched {} TiposDePrecio", tiposDePrecio.getTotalElements());
        return ResponseEntity.ok(tiposDePrecio);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoDePrecioDTO> getTipoDePrecioById(@PathVariable Integer id) {
        logger.info("Fetching TipoDePrecio with ID: {}", id);
        TipoDePrecioDTO tipoDePrecioDTO = tipoDePrecioService.findOne(id);
        if (tipoDePrecioDTO == null) {
            logger.warn("TipoDePrecio with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        }
        logger.info("TipoDePrecio with ID: {} found", id);
        return ResponseEntity.ok(tipoDePrecioDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoDePrecioDTO> updateTipoDePrecio(@PathVariable Integer id, @RequestBody TipoDePrecioDTO tipoDePrecioDTO) {
        logger.info("Updating TipoDePrecio with ID: {}", id);
        if (tipoDePrecioDTO.getTipoDePrecioId() == null || !tipoDePrecioDTO.getTipoDePrecioId().equals(id)) {
            logger.warn("Invalid ID provided in request: expected ID: {}, received ID: {}", id, tipoDePrecioDTO.getTipoDePrecioId());
            return ResponseEntity.badRequest().body(null); // ID en el DTO debe coincidir con el ID en la URL
        }
        TipoDePrecioDTO updatedTipoDePrecio = tipoDePrecioService.update(tipoDePrecioDTO);
        if (updatedTipoDePrecio == null) {
            logger.warn("TipoDePrecio with ID: {} not found for update", id);
            return ResponseEntity.notFound().build();
        }
        logger.info("TipoDePrecio with ID: {} updated successfully", id);
        return ResponseEntity.ok(updatedTipoDePrecio);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipoDePrecio(@PathVariable Integer id) {
        logger.info("Deleting TipoDePrecio with ID: {}", id);
        try {
            tipoDePrecioService.delete(id);
            logger.info("TipoDePrecio with ID: {} deleted successfully", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            logger.error("Failed to delete TipoDePrecio with ID: {}. Error: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
