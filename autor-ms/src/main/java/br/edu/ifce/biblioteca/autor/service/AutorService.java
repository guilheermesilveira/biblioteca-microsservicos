package br.edu.ifce.biblioteca.autor.service;

import br.edu.ifce.biblioteca.autor.dto.AutorRequest;
import br.edu.ifce.biblioteca.autor.dto.AutorResponse;
import br.edu.ifce.biblioteca.autor.entity.Autor;
import br.edu.ifce.biblioteca.autor.exception.AutorNotFoundException;
import br.edu.ifce.biblioteca.autor.repository.AutorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AutorService {

    private final AutorRepository autorRepository;

    public AutorService(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    @Transactional(readOnly = true)
    public List<AutorResponse> listar() {
        return autorRepository.findAll()
                .stream()
                .map(AutorResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public AutorResponse buscar(Long id) {
        return AutorResponse.fromEntity(buscarEntidade(id));
    }

    @Transactional
    public AutorResponse cadastrar(AutorRequest request) {
        Autor autor = new Autor(
                request.nome().trim(),
                request.nacionalidade().trim(),
                request.anoNascimento()
        );
        return AutorResponse.fromEntity(autorRepository.save(autor));
    }

    @Transactional
    public AutorResponse atualizar(Long id, AutorRequest request) {
        Autor autor = buscarEntidade(id);
        autor.atualizar(
                request.nome().trim(),
                request.nacionalidade().trim(),
                request.anoNascimento()
        );
        return AutorResponse.fromEntity(autor);
    }

    @Transactional
    public void excluir(Long id) {
        Autor autor = buscarEntidade(id);
        autorRepository.delete(autor);
    }

    private Autor buscarEntidade(Long id) {
        return autorRepository.findById(id)
                .orElseThrow(() -> new AutorNotFoundException(id));
    }
}
