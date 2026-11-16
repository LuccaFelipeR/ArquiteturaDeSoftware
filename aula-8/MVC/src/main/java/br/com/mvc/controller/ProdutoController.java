package br.com.mvc.controller;

import br.com.mvc.service.ProdutoService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model,
            @SessionAttribute(name = "perfil", required = false) String perfil) {

        model.addAttribute("produtos", service.listar());
        model.addAttribute("admin", "ADM".equals(perfil));

        return "produtos";
    }

    @GetMapping("/perfil/{perfil}")
    public String alterarPerfil(@PathVariable String perfil,
            HttpSession session) {

        if (!perfil.equals("ADM") && !perfil.equals("USUARIO")) {
            return "redirect:/produtos";
        }

        session.setAttribute("perfil", perfil);
        return "redirect:/produtos";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(@RequestParam String nome,
            RedirectAttributes redirect) {
        try {
            service.cadastrar(nome);
            redirect.addFlashAttribute("mensagem", "Produto cadastrado");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
        }

        return "redirect:/produtos";
    }

    @PostMapping("/{id}/entrada")
    public String entrada(@PathVariable Long id,
            @RequestParam int quantidade,
            RedirectAttributes redirect) {
        try {
            service.entrada(id, quantidade);
            redirect.addFlashAttribute("mensagem", "Entrada realizada");
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
        }

        return "redirect:/produtos";
    }

    @PostMapping("/{id}/saida")
    public String saida(@PathVariable Long id,
            @RequestParam int quantidade,
            RedirectAttributes redirect) {
        try {
            service.saida(id, quantidade);
            redirect.addFlashAttribute("mensagem", "Saída realizada");
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
        }

        return "redirect:/produtos";
    }

    @PostMapping("/{id}/descontinuar")
    public String descontinuar(@PathVariable Long id,
            @SessionAttribute(name = "perfil", required = false) String perfil,
            RedirectAttributes redirect) {

        try {
            boolean admin = "ADM".equals(perfil);

            service.descontinuar(id, admin);
            redirect.addFlashAttribute("mensagem", "Produto descontinuado");

        } catch (IllegalArgumentException | IllegalStateException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
        }

        return "redirect:/produtos";
    }

}
