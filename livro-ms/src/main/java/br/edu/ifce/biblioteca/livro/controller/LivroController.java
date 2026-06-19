package br.edu.ifce.biblioteca.livro.controller;

import br.edu.ifce.biblioteca.livro.dto.LivroRequest;
import br.edu.ifce.biblioteca.livro.dto.LivroResponse;
import br.edu.ifce.biblioteca.livro.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @GetMapping
    public List<LivroResponse> listar(@RequestParam(required = false) Boolean disponivel) {
        return livroService.listar(disponivel);
    }

    @GetMapping("/{id}")
    public LivroResponse buscar(@PathVariable Long id) {
        return livroService.buscar(id);
    }

    @PostMapping
    public ResponseEntity<LivroResponse> cadastrar(@Valid @RequestBody LivroRequest request) {
        LivroResponse livro = livroService.cadastrar(request);
        return ResponseEntity
                .created(URI.create("/api/livros/" + livro.id()))
                .body(livro);
    }

    @PutMapping("/{id}")
    public LivroResponse atualizar(@PathVariable Long id, @Valid @RequestBody LivroRequest request) {
        return livroService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        livroService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
