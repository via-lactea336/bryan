package com.miapp.reportes.controller;

import com.miapp.reportes.services.categoria.ICategoriaService;
import com.miapp.sistemasdistribuidos.dto.CategoriaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private static final Logger logger = LoggerFactory.getLogger(CategoriaController.class);

    @Autowired
    private ICategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<CategoriaDTO> createCategoria(@RequestBody CategoriaDTO categoriaDTO) {
        logger.info("Creating new category: {}", categoriaDTO);
        CategoriaDTO savedCategoria = categoriaService.save(categoriaDTO);
        logger.info("Category created successfully with ID: {}", savedCategoria.getCategoriaId());
        return ResponseEntity.ok(savedCategoria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> updateCategoria(@PathVariable Integer id, @RequestBody CategoriaDTO categoriaDTO) {
        logger.info("Updating category with ID: {}", id);
        CategoriaDTO updatedCategoria = categoriaService.update(id, categoriaDTO);
        if (updatedCategoria == null) {
            logger.warn("Category with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        }
        logger.info("Category with ID: {} updated successfully", id);
        return ResponseEntity.ok(updatedCategoria);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Integer id) {
        logger.info("Deleting category with ID: {}", id);
        boolean deleted = categoriaService.delete(id);
        if (!deleted) {
            logger.warn("Category with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        }
        logger.info("Category with ID: {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> getCategoriaById(@PathVariable Integer id) {
        logger.info("Fetching category with ID: {}", id);
        CategoriaDTO categoriaDTO = categoriaService.getById(id);
        if (categoriaDTO == null) {
            logger.warn("Category with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        }
        logger.info("Category with ID: {} found", id);
        return ResponseEntity.ok(categoriaDTO);
    }

    @GetMapping
    public ResponseEntity<Page<CategoriaDTO>> getAllCategorias(Pageable pageable) {
        logger.info("Fetching all categories with pagination");
        Page<CategoriaDTO> categorias = categoriaService.getAll(pageable);
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CategoriaDTO>> findByNombre(@RequestParam String nombreCategoria, Pageable pageable) {
        logger.info("Searching categories by name: {}", nombreCategoria);
        Page<CategoriaDTO> categorias = categoriaService.findByNombre(nombreCategoria, pageable);
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/search/containing")
    public ResponseEntity<Page<CategoriaDTO>> findByNombreContaining(@RequestParam String nombreCategoria, Pageable pageable) {
        logger.info("Searching categories containing name: {}", nombreCategoria);
        Page<CategoriaDTO> categorias = categoriaService.findByNombreContaining(nombreCategoria, pageable);
        return ResponseEntity.ok(categorias);
    }
}
