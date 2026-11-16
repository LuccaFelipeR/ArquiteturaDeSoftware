package br.com.mvc.controller;

import br.com.mvc.model.PedidoVenda;
import br.com.mvc.service.VendaService;
import java.math.BigDecimal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/vendas")
public class VendaController {

    private final VendaService service;

    public VendaController(VendaService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("pedidos", service.listar());
        return "vendas";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(@RequestParam String descricao,
                           @RequestParam String clienteEmail,
                           @RequestParam BigDecimal valor,
                           RedirectAttributes redirect) {
        try {
            PedidoVenda pedido = service.cadastrar(descricao, clienteEmail, valor);
            redirect.addFlashAttribute("mensagem", "Pedido " + pedido.getCodigoPedido() + " cadastrado!");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/vendas";
    }

    @PostMapping("/{id}/fechar")
    public String fechar(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            PedidoVenda pedido = service.fechar(id);
            redirect.addFlashAttribute("mensagem", "Pedido " + pedido.getCodigoPedido()
                    + " fechado! Fatura ID: " + pedido.getFaturaId());
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/vendas";
    }
}
