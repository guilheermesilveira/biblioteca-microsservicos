package br.edu.ifce.biblioteca.front.service;

import br.edu.ifce.biblioteca.front.client.ApiNotFoundException;
import br.edu.ifce.biblioteca.front.client.AutorApiClient;
import br.edu.ifce.biblioteca.front.client.LivroApiClient;
import br.edu.ifce.biblioteca.front.dto.AutorDto;
import br.edu.ifce.biblioteca.front.dto.LivroDto;
import br.edu.ifce.biblioteca.front.dto.LivroForm;
import br.edu.ifce.biblioteca.front.dto.LivroListaView;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivroFrontService {

    private final LivroApiClient livroApiClient;
    private final AutorApiClient autorApiClient;

    public LivroFrontService(LivroApiClient livroApiClient, AutorApiClient autorApiClient) {
        this.livroApiClient = livroApiClient;
        this.autorApiClient = autorApiClient;
    }

    public List<LivroListaView> listarComAutores(Boolean disponivel) {
        return livroApiClient.listar(disponivel)
                .stream()
                .map(livro -> LivroListaView.fromLivro(livro, resolverNomeAutor(livro.autorId())))
                .toList();
    }

    public LivroDto buscar(Long id) {
        return livroApiClient.buscar(id);
    }

    public String resolverNomeAutor(Long autorId) {
        try {
            AutorDto autor = autorApiClient.buscar(autorId);
            return autor.nome();
        } catch (ApiNotFoundException exception) {
            return "Autor removido";
        }
    }

    public LivroDto cadastrar(LivroForm form) {
        autorApiClient.buscar(form.getAutorId());
        return livroApiClient.cadastrar(form.toDto());
    }

    public LivroDto atualizar(Long id, LivroForm form) {
        autorApiClient.buscar(form.getAutorId());
        return livroApiClient.atualizar(id, form.toDto());
    }

    public void excluir(Long id) {
        livroApiClient.excluir(id);
    }
}
