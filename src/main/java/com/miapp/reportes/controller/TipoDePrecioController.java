package com.miapp.reportes.controller;

import com.miapp.sistemasdistribuidos.dto.TipoDePrecioDTO;
import com.miapp.reportes.services.tipoDePrecio.ITipoDePrecio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tipos-de-precio")
public class TipoDePrecioController {

    @Autowired
    private ITipoDePrecio tipoDePrecioService;

    @PostMapping
    public ResponseEntity<TipoDePrecioDTO> createTipoDePrecio(@RequestBody TipoDePrecioDTO tipoDePrecioDTO) {
        TipoDePrecioDTO savedTipoDePrecio = tipoDePrecioService.save(tipoDePrecioDTO);
        return ResponseEntity.ok(savedTipoDePrecio);
    }

    @GetMapping
    public ResponseEntity<Page<TipoDePrecioDTO>> getAllTipoDePrecios(Pageable pageable) {
        Page<TipoDePrecioDTO> tiposDePrecio = tipoDePrecioService.findAll(pageable);
        return ResponseEntity.ok(tiposDePrecio);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoDePrecioDTO> getTipoDePrecioById(@PathVariable Integer id) {
        TipoDePrecioDTO tipoDePrecioDTO = tipoDePrecioService.findOne(id);
        if (tipoDePrecioDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tipoDePrecioDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoDePrecioDTO> updateTipoDePrecio(@PathVariable Integer id, @RequestBody TipoDePrecioDTO tipoDePrecioDTO) {
        if (tipoDePrecioDTO.getTipoDePrecioId() == null || !tipoDePrecioDTO.getTipoDePrecioId().equals(id)) {
            return ResponseEntity.badRequest().body(null); // ID en el DTO debe coincidir con el ID en la URL
        }
        TipoDePrecioDTO updatedTipoDePrecio = tipoDePrecioService.update(tipoDePrecioDTO);
        if (updatedTipoDePrecio == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedTipoDePrecio);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipoDePrecio(@PathVariable Integer id) {
        try {
            tipoDePrecioService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}