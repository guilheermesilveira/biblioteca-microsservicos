package br.edu.ifce.biblioteca.autor.controller;

import br.edu.ifce.biblioteca.autor.dto.AutorRequest;
import br.edu.ifce.biblioteca.autor.dto.AutorResponse;
import br.edu.ifce.biblioteca.autor.service.AutorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/autores")
public class AutorController {

    private final AutorService autorService;

    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    @GetMapping
    public List<AutorResponse> listar() {
        return autorService.listar();
    }

    @GetMapping("/{id}")
    public AutorResponse buscar(@PathVariable Long id) {
        return autorService.buscar(id);
    }

    @PostMapping
    public ResponseEntity<AutorResponse> cadastrar(@Valid @RequestBody AutorRequest request) {
        AutorResponse autor = autorService.cadastrar(request);
        return ResponseEntity
                .created(URI.create("/api/autores/" + autor.id()))
                .body(autor);
    }

    @PutMapping("/{id}")
    public AutorResponse atualizar(@PathVariable Long id, @Valid @RequestBody AutorRequest request) {
        return autorService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        autorService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
