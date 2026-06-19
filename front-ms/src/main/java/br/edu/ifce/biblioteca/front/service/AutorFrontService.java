package br.edu.ifce.biblioteca.front.service;

import br.edu.ifce.biblioteca.front.client.AutorApiClient;
import br.edu.ifce.biblioteca.front.dto.AutorDto;
import br.edu.ifce.biblioteca.front.dto.AutorForm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutorFrontService {

    private final AutorApiClient autorApiClient;

    public AutorFrontService(AutorApiClient autorApiClient) {
        this.autorApiClient = autorApiClient;
    }

    public List<AutorDto> listar() {
        return autorApiClient.listar();
    }

    public AutorDto buscar(Long id) {
        return autorApiClient.buscar(id);
    }

    public AutorDto cadastrar(AutorForm form) {
        return autorApiClient.cadastrar(form.toDto());
    }

    public AutorDto atualizar(Long id, AutorForm form) {
        return autorApiClient.atualizar(id, form.toDto());
    }

    public void excluir(Long id) {
        autorApiClient.excluir(id);
    }
}
