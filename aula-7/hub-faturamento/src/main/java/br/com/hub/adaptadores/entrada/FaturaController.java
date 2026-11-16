package br.com.hub.adaptadores.entrada;

import br.com.hub.dominio.Fatura;
import br.com.hub.dominio.Pedido;
import br.com.hub.dominio.portas.FaturarPedidoUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/faturas")
public class FaturaController {
    private final FaturarPedidoUseCase faturamento;

    public FaturaController(FaturarPedidoUseCase faturamento) {
        this.faturamento = faturamento;
    }

    @PostMapping
    public ResponseEntity<Fatura> criar(@RequestBody PedidoRequest request) {
        Fatura fatura = faturamento.faturar(new Pedido(
                request.pedidoId(), request.clienteEmail(), request.valor()));
        return ResponseEntity.status(HttpStatus.CREATED).body(fatura);
    }

    @GetMapping("/{id}")
    public Fatura consultar(@PathVariable Long id) {
        try {
            return faturamento.consultar(id);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> erro(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
    }

    public record PedidoRequest(String pedidoId, String clienteEmail, BigDecimal valor) {
    }
}
