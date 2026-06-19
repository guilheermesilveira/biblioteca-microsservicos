package br.edu.ifce.biblioteca.front;

import br.edu.ifce.biblioteca.front.client.ApiNotFoundException;
import br.edu.ifce.biblioteca.front.client.AutorApiClient;
import br.edu.ifce.biblioteca.front.client.LivroApiClient;
import br.edu.ifce.biblioteca.front.dto.AutorDto;
import br.edu.ifce.biblioteca.front.dto.LivroDto;
import br.edu.ifce.biblioteca.front.dto.LivroForm;
import br.edu.ifce.biblioteca.front.dto.LivroListaView;
import br.edu.ifce.biblioteca.front.service.LivroFrontService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LivroFrontServiceTest {

    private final FakeLivroApiClient livroApiClient = new FakeLivroApiClient();
    private final FakeAutorApiClient autorApiClient = new FakeAutorApiClient();
    private final LivroFrontService livroFrontService = new LivroFrontService(livroApiClient, autorApiClient);

    @Test
    void deveExibirAutorRemovidoQuandoAutorNaoExiste() {
        livroApiClient.livros = List.of(
                new LivroDto(1L, "Livro sem autor", "Drama", 2001, true, 99L)
        );
        autorApiClient.autorInexistente = 99L;

        List<LivroListaView> livros = livroFrontService.listarComAutores(null);

        assertThat(livros).hasSize(1);
        assertThat(livros.getFirst().autorNome()).isEqualTo("Autor removido");
    }

    @Test
    void deveValidarAutorAntesDeCadastrarLivro() {
        LivroForm form = new LivroForm("1984", "Distopia", 1949, true, 3L);
        autorApiClient.autor = new AutorDto(3L, "George Orwell", "Britanica", 1903);
        livroApiClient.livroCadastrado = new LivroDto(1L, "1984", "Distopia", 1949, true, 3L);

        livroFrontService.cadastrar(form);

        assertThat(autorApiClient.autorBuscado).isEqualTo(3L);
        assertThat(livroApiClient.livroRecebido).isEqualTo(form.toDto());
    }

    private static class FakeLivroApiClient extends LivroApiClient {

        private List<LivroDto> livros = List.of();
        private LivroDto livroCadastrado;
        private LivroDto livroRecebido;

        FakeLivroApiClient() {
            super(null, null);
        }

        @Override
        public List<LivroDto> listar(Boolean disponivel) {
            return livros;
        }

        @Override
        public LivroDto cadastrar(LivroDto livro) {
            livroRecebido = livro;
            return livroCadastrado;
        }
    }

    private static class FakeAutorApiClient extends AutorApiClient {

        private AutorDto autor;
        private Long autorInexistente;
        private Long autorBuscado;

        FakeAutorApiClient() {
            super(null, null);
        }

        @Override
        public AutorDto buscar(Long id) {
            autorBuscado = id;
            if (id.equals(autorInexistente)) {
                throw new ApiNotFoundException("Autor nao encontrado.");
            }
            return autor;
        }
    }
}
