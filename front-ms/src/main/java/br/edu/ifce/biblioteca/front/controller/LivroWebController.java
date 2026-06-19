package br.edu.ifce.biblioteca.front.controller;

import br.edu.ifce.biblioteca.front.client.ApiNotFoundException;
import br.edu.ifce.biblioteca.front.client.ApiUnavailableException;
import br.edu.ifce.biblioteca.front.client.ApiValidationException;
import br.edu.ifce.biblioteca.front.dto.LivroDto;
import br.edu.ifce.biblioteca.front.dto.LivroForm;
import br.edu.ifce.biblioteca.front.service.AutorFrontService;
import br.edu.ifce.biblioteca.front.service.LivroFrontService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/livros")
public class LivroWebController {

    private final LivroFrontService livroFrontService;
    private final AutorFrontService autorFrontService;

    public LivroWebController(LivroFrontService livroFrontService, AutorFrontService autorFrontService) {
        this.livroFrontService = livroFrontService;
        this.autorFrontService = autorFrontService;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) Boolean disponivel, Model model) {
        try {
            model.addAttribute("livros", livroFrontService.listarComAutores(disponivel));
        } catch (ApiUnavailableException exception) {
            model.addAttribute("livros", List.of());
            model.addAttribute("erro", exception.getMessage());
        }
        model.addAttribute("filtroDisponivel", disponivel);
        return "livros/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        prepararFormulario(model, new LivroForm(), false, null);
        return "livros/formulario";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            prepararFormulario(model, LivroForm.fromDto(livroFrontService.buscar(id)), true, id);
            return "livros/formulario";
        } catch (ApiNotFoundException | ApiUnavailableException exception) {
            redirectAttributes.addFlashAttribute("erro", exception.getMessage());
            return "redirect:/livros";
        }
    }

    @GetMapping("/{id}")
    public String detalhe(@PathVariable Long id, Model model) {
        try {
            LivroDto livro = livroFrontService.buscar(id);
            model.addAttribute("livro", livro);
            model.addAttribute("autorNome", livroFrontService.resolverNomeAutor(livro.autorId()));
            return "livros/detalhe";
        } catch (ApiNotFoundException exception) {
            model.addAttribute("titulo", "Livro nao encontrado");
            model.addAttribute("mensagem", exception.getMessage());
            return "error/amigavel";
        } catch (ApiUnavailableException exception) {
            model.addAttribute("titulo", "Servico indisponivel");
            model.addAttribute("mensagem", exception.getMessage());
            return "error/amigavel";
        }
    }

    @PostMapping
    public String cadastrar(@Valid @ModelAttribute("livroForm") LivroForm livroForm,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            prepararFormulario(model, livroForm, false, null);
            return "livros/formulario";
        }

        try {
            livroFrontService.cadastrar(livroForm);
            redirectAttributes.addFlashAttribute("sucesso", "Livro cadastrado com sucesso.");
            return "redirect:/livros";
        } catch (ApiValidationException exception) {
            model.addAttribute("apiErrors", exception.getErrors());
            prepararFormulario(model, livroForm, false, null);
            return "livros/formulario";
        } catch (ApiNotFoundException exception) {
            bindingResult.rejectValue("autorId", "autorId", "Autor selecionado nao foi encontrado.");
            prepararFormulario(model, livroForm, false, null);
            return "livros/formulario";
        } catch (ApiUnavailableException exception) {
            model.addAttribute("erro", exception.getMessage());
            prepararFormulario(model, livroForm, false, null);
            return "livros/formulario";
        }
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id,
                            @Valid @ModelAttribute("livroForm") LivroForm livroForm,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            prepararFormulario(model, livroForm, true, id);
            return "livros/formulario";
        }

        try {
            livroFrontService.atualizar(id, livroForm);
            redirectAttributes.addFlashAttribute("sucesso", "Livro atualizado com sucesso.");
            return "redirect:/livros";
        } catch (ApiValidationException exception) {
            model.addAttribute("apiErrors", exception.getErrors());
            prepararFormulario(model, livroForm, true, id);
            return "livros/formulario";
        } catch (ApiNotFoundException exception) {
            bindingResult.rejectValue("autorId", "autorId", exception.getMessage());
            prepararFormulario(model, livroForm, true, id);
            return "livros/formulario";
        } catch (ApiUnavailableException exception) {
            model.addAttribute("erro", exception.getMessage());
            prepararFormulario(model, livroForm, true, id);
            return "livros/formulario";
        }
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            livroFrontService.excluir(id);
            redirectAttributes.addFlashAttribute("sucesso", "Livro excluído com sucesso.");
        } catch (ApiNotFoundException | ApiUnavailableException exception) {
            redirectAttributes.addFlashAttribute("erro", exception.getMessage());
        }
        return "redirect:/livros";
    }

    private void prepararFormulario(Model model, LivroForm livroForm, boolean modoEdicao, Long livroId) {
        model.addAttribute("livroForm", livroForm);
        model.addAttribute("modoEdicao", modoEdicao);
        model.addAttribute("livroId", livroId);
        try {
            model.addAttribute("autores", autorFrontService.listar());
        } catch (ApiUnavailableException exception) {
            model.addAttribute("autores", List.of());
            model.addAttribute("erro", exception.getMessage());
        }
    }
}
