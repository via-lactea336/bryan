package com.miapp.reportes.controller;

import com.miapp.reportes.services.categoria.ICategoriaService;
import com.miapp.sistemasdistribuidos.dto.CategoriaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private ICategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<CategoriaDTO> createCategoria(@RequestBody CategoriaDTO categoriaDTO) {
        CategoriaDTO savedCategoria = categoriaService.save(categoriaDTO);
        return ResponseEntity.ok(savedCategoria);
    }
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> updateCategoria(@PathVariable Integer id, @RequestBody CategoriaDTO categoriaDTO) {
        CategoriaDTO updatedCategoria = categoriaService.update(id, categoriaDTO);
        if (updatedCategoria == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedCategoria);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Integer id) {
        boolean deleted = categoriaService.delete(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> getCategoriaById(@PathVariable Integer id) {
        CategoriaDTO categoriaDTO = categoriaService.getById(id);
        if (categoriaDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categoriaDTO);
    }

    @GetMapping
    public ResponseEntity<Page<CategoriaDTO>> getAllCategorias(Pageable pageable) {
        Page<CategoriaDTO> categorias = categoriaService.getAll(pageable);
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CategoriaDTO>> findByNombre(@RequestParam String nombreCategoria) {
        List<CategoriaDTO> categorias = categoriaService.findByNombre(nombreCategoria);
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/search/containing")
    public ResponseEntity<Page<CategoriaDTO>> findByNombreContaining(@RequestParam String nombreCategoria, Pageable pageable) {
        Page<CategoriaDTO> categorias = categoriaService.findByNombreContaining(nombreCategoria, pageable);
        return ResponseEntity.ok(categorias);
    }
}
