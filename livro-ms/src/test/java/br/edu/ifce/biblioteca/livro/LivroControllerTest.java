package br.edu.ifce.biblioteca.livro;

import br.edu.ifce.biblioteca.livro.controller.LivroController;
import br.edu.ifce.biblioteca.livro.controller.RestExceptionHandler;
import br.edu.ifce.biblioteca.livro.dto.LivroRequest;
import br.edu.ifce.biblioteca.livro.dto.LivroResponse;
import br.edu.ifce.biblioteca.livro.exception.LivroNotFoundException;
import br.edu.ifce.biblioteca.livro.service.LivroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LivroControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private LivroService livroService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        livroService = new FakeLivroService();

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders
                .standaloneSetup(new LivroController(livroService))
                .setControllerAdvice(new RestExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    void deveListarLivrosComStatus200() throws Exception {
        ((FakeLivroService) livroService).livros = List.of(
                new LivroResponse(1L, "1984", "Distopia", 1949, false, 3L)
        );

        mockMvc.perform(get("/api/livros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].titulo").value("1984"))
                .andExpect(jsonPath("$[0].genero").value("Distopia"))
                .andExpect(jsonPath("$[0].anoPublicacao").value(1949))
                .andExpect(jsonPath("$[0].disponivel").value(false))
                .andExpect(jsonPath("$[0].autorId").value(3));
    }

    @Test
    void deveEncaminharFiltroDeDisponibilidade() throws Exception {
        mockMvc.perform(get("/api/livros").param("disponivel", "true"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        org.assertj.core.api.Assertions.assertThat(((FakeLivroService) livroService).ultimoFiltro).isTrue();
    }

    @Test
    void deveRetornar404QuandoLivroNaoExiste() throws Exception {
        ((FakeLivroService) livroService).livroInexistente = 999L;

        mockMvc.perform(get("/api/livros/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").value("Livro não encontrado: 999"));
    }

    @Test
    void deveCadastrarLivroComStatus201() throws Exception {
        ((FakeLivroService) livroService).livroCadastrado =
                new LivroResponse(10L, "Dom Casmurro", "Romance", 1899, true, 1L);

        LivroRequest request = new LivroRequest("Dom Casmurro", "Romance", 1899, true, 1L);

        mockMvc.perform(post("/api/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.titulo").value("Dom Casmurro"));
    }

    @Test
    void deveRetornar400QuandoCadastroInvalido() throws Exception {
        LivroRequest request = new LivroRequest("", "", -1, true, null);

        mockMvc.perform(post("/api/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros.titulo").exists())
                .andExpect(jsonPath("$.erros.genero").exists())
                .andExpect(jsonPath("$.erros.anoPublicacao").exists())
                .andExpect(jsonPath("$.erros.autorId").exists());
    }

    @Test
    void deveExcluirLivroComStatus204() throws Exception {
        mockMvc.perform(delete("/api/livros/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        org.assertj.core.api.Assertions.assertThat(((FakeLivroService) livroService).livroExcluido).isEqualTo(1L);
    }

    private static class FakeLivroService extends LivroService {

        private List<LivroResponse> livros = List.of();
        private Boolean ultimoFiltro;
        private Long livroInexistente;
        private LivroResponse livroCadastrado;
        private Long livroExcluido;

        FakeLivroService() {
            super(null);
        }

        @Override
        public List<LivroResponse> listar(Boolean disponivel) {
            ultimoFiltro = disponivel;
            if (disponivel != null) {
                return List.of();
            }
            return livros;
        }

        @Override
        public LivroResponse buscar(Long id) {
            if (id.equals(livroInexistente)) {
                throw new LivroNotFoundException(id);
            }
            return new LivroResponse(id, "1984", "Distopia", 1949, false, 3L);
        }

        @Override
        public LivroResponse cadastrar(LivroRequest request) {
            return livroCadastrado;
        }

        @Override
        public void excluir(Long id) {
            livroExcluido = id;
        }
    }
}
