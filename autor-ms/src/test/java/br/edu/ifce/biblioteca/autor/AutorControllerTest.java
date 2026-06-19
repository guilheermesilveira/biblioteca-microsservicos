package br.edu.ifce.biblioteca.autor;

import br.edu.ifce.biblioteca.autor.controller.AutorController;
import br.edu.ifce.biblioteca.autor.controller.RestExceptionHandler;
import br.edu.ifce.biblioteca.autor.dto.AutorRequest;
import br.edu.ifce.biblioteca.autor.dto.AutorResponse;
import br.edu.ifce.biblioteca.autor.exception.AutorNotFoundException;
import br.edu.ifce.biblioteca.autor.service.AutorService;
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

class AutorControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private AutorService autorService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        autorService = new FakeAutorService();

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders
                .standaloneSetup(new AutorController(autorService))
                .setControllerAdvice(new RestExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    void deveListarAutoresComStatus200() throws Exception {
        ((FakeAutorService) autorService).autores = List.of(
                new AutorResponse(1L, "Machado de Assis", "Brasileira", 1839)
        );

        mockMvc.perform(get("/api/autores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("Machado de Assis"))
                .andExpect(jsonPath("$[0].nacionalidade").value("Brasileira"))
                .andExpect(jsonPath("$[0].anoNascimento").value(1839));
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHaAutores() throws Exception {
        ((FakeAutorService) autorService).autores = List.of();

        mockMvc.perform(get("/api/autores"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void deveRetornar404QuandoAutorNaoExiste() throws Exception {
        ((FakeAutorService) autorService).autorInexistente = 999L;

        mockMvc.perform(get("/api/autores/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").value("Autor não encontrado: 999"));
    }

    @Test
    void deveCadastrarAutorComStatus201() throws Exception {
        ((FakeAutorService) autorService).autorCadastrado =
                new AutorResponse(10L, "Clarice Lispector", "Brasileira", 1920);

        AutorRequest request = new AutorRequest("Clarice Lispector", "Brasileira", 1920);

        mockMvc.perform(post("/api/autores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.nome").value("Clarice Lispector"));
    }

    @Test
    void deveRetornar400QuandoCadastroInvalido() throws Exception {
        AutorRequest request = new AutorRequest("", "", -1);

        mockMvc.perform(post("/api/autores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros.nome").exists())
                .andExpect(jsonPath("$.erros.nacionalidade").exists())
                .andExpect(jsonPath("$.erros.anoNascimento").exists());
    }

    @Test
    void deveExcluirAutorComStatus204() throws Exception {
        mockMvc.perform(delete("/api/autores/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        org.assertj.core.api.Assertions.assertThat(((FakeAutorService) autorService).autorExcluido).isEqualTo(1L);
    }

    private static class FakeAutorService extends AutorService {

        private List<AutorResponse> autores = List.of();
        private Long autorInexistente;
        private AutorResponse autorCadastrado;
        private Long autorExcluido;

        FakeAutorService() {
            super(null);
        }

        @Override
        public List<AutorResponse> listar() {
            return autores;
        }

        @Override
        public AutorResponse buscar(Long id) {
            if (id.equals(autorInexistente)) {
                throw new AutorNotFoundException(id);
            }
            return new AutorResponse(id, "Machado de Assis", "Brasileira", 1839);
        }

        @Override
        public AutorResponse cadastrar(AutorRequest request) {
            return autorCadastrado;
        }

        @Override
        public void excluir(Long id) {
            autorExcluido = id;
        }
    }
}
