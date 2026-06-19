package br.edu.ifce.biblioteca.front.controller;

import br.edu.ifce.biblioteca.front.client.ApiNotFoundException;
import br.edu.ifce.biblioteca.front.client.ApiUnavailableException;
import br.edu.ifce.biblioteca.front.client.ApiValidationException;
import br.edu.ifce.biblioteca.front.dto.AutorForm;
import br.edu.ifce.biblioteca.front.service.AutorFrontService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/autores")
public class AutorWebController {

    private final AutorFrontService autorFrontService;

    public AutorWebController(AutorFrontService autorFrontService) {
        this.autorFrontService = autorFrontService;
    }

    @GetMapping
    public String listar(Model model) {
        try {
            model.addAttribute("autores", autorFrontService.listar());
        } catch (ApiUnavailableException exception) {
            model.addAttribute("autores", java.util.List.of());
            model.addAttribute("erro", exception.getMessage());
        }
        return "autores/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        prepararFormulario(model, new AutorForm(), false, null);
        return "autores/formulario";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            prepararFormulario(model, AutorForm.fromDto(autorFrontService.buscar(id)), true, id);
            return "autores/formulario";
        } catch (ApiNotFoundException | ApiUnavailableException exception) {
            redirectAttributes.addFlashAttribute("erro", exception.getMessage());
            return "redirect:/autores";
        }
    }

    @PostMapping
    public String cadastrar(@Valid @ModelAttribute("autorForm") AutorForm autorForm,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            prepararFormulario(model, autorForm, false, null);
            return "autores/formulario";
        }

        try {
            autorFrontService.cadastrar(autorForm);
            redirectAttributes.addFlashAttribute("sucesso", "Autor cadastrado com sucesso.");
            return "redirect:/autores";
        } catch (ApiValidationException exception) {
            model.addAttribute("apiErrors", exception.getErrors());
            prepararFormulario(model, autorForm, false, null);
            return "autores/formulario";
        } catch (ApiUnavailableException exception) {
            model.addAttribute("erro", exception.getMessage());
            prepararFormulario(model, autorForm, false, null);
            return "autores/formulario";
        }
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id,
                            @Valid @ModelAttribute("autorForm") AutorForm autorForm,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            prepararFormulario(model, autorForm, true, id);
            return "autores/formulario";
        }

        try {
            autorFrontService.atualizar(id, autorForm);
            redirectAttributes.addFlashAttribute("sucesso", "Autor atualizado com sucesso.");
            return "redirect:/autores";
        } catch (ApiValidationException exception) {
            model.addAttribute("apiErrors", exception.getErrors());
            prepararFormulario(model, autorForm, true, id);
            return "autores/formulario";
        } catch (ApiNotFoundException | ApiUnavailableException exception) {
            model.addAttribute("erro", exception.getMessage());
            prepararFormulario(model, autorForm, true, id);
            return "autores/formulario";
        }
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            autorFrontService.excluir(id);
            redirectAttributes.addFlashAttribute("sucesso", "Autor excluido com sucesso.");
        } catch (ApiNotFoundException | ApiUnavailableException exception) {
            redirectAttributes.addFlashAttribute("erro", exception.getMessage());
        }
        return "redirect:/autores";
    }

    private void prepararFormulario(Model model, AutorForm autorForm, boolean modoEdicao, Long autorId) {
        model.addAttribute("autorForm", autorForm);
        model.addAttribute("modoEdicao", modoEdicao);
        model.addAttribute("autorId", autorId);
    }
}
