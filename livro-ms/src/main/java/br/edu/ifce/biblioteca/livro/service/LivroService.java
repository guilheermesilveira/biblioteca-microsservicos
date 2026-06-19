package br.edu.ifce.biblioteca.livro.service;

import br.edu.ifce.biblioteca.livro.dto.LivroRequest;
import br.edu.ifce.biblioteca.livro.dto.LivroResponse;
import br.edu.ifce.biblioteca.livro.entity.Livro;
import br.edu.ifce.biblioteca.livro.exception.LivroNotFoundException;
import br.edu.ifce.biblioteca.livro.repository.LivroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LivroService {

    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    @Transactional(readOnly = true)
    public List<LivroResponse> listar(Boolean disponivel) {
        List<Livro> livros = disponivel == null
                ? livroRepository.findAll()
                : livroRepository.findByDisponivel(disponivel);

        return livros.stream()
                .map(LivroResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public LivroResponse buscar(Long id) {
        return LivroResponse.fromEntity(buscarEntidade(id));
    }

    @Transactional
    public LivroResponse cadastrar(LivroRequest request) {
        Livro livro = new Livro(
                request.titulo().trim(),
                request.genero().trim(),
                request.anoPublicacao(),
                request.disponivelOuPadrao(),
                request.autorId()
        );
        return LivroResponse.fromEntity(livroRepository.save(livro));
    }

    @Transactional
    public LivroResponse atualizar(Long id, LivroRequest request) {
        Livro livro = buscarEntidade(id);
        livro.atualizar(
                request.titulo().trim(),
                request.genero().trim(),
                request.anoPublicacao(),
                request.disponivelOuPadrao(),
                request.autorId()
        );
        return LivroResponse.fromEntity(livro);
    }

    @Transactional
    public void excluir(Long id) {
        Livro livro = buscarEntidade(id);
        livroRepository.delete(livro);
    }

    private Livro buscarEntidade(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new LivroNotFoundException(id));
    }
}
