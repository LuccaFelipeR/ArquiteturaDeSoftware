package br.com.mvc.service;

import br.com.mvc.integracao.HubFaturamentoClient;
import br.com.mvc.model.PedidoVenda;
import br.com.mvc.model.StatusPedido;
import br.com.mvc.repository.PedidoVendaRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class VendaService {

    private final PedidoVendaRepository repository;
    private final HubFaturamentoClient hub;

    public VendaService(PedidoVendaRepository repository, HubFaturamentoClient hub) {
        this.repository = repository;
        this.hub = hub;
    }

    public List<PedidoVenda> listar() {
        return repository.findAllByOrderByIdDesc();
    }

    public PedidoVenda cadastrar(String descricao, String email, BigDecimal valor) {
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Informe o produto ou a descricao da venda");
        }
        if (email == null || !email.contains("@") || email.isBlank()) {
            throw new IllegalArgumentException("Informe um e-mail valido");
        }
        if (valor == null || valor.signum() <= 0) {
            throw new IllegalArgumentException("O valor precisa ser maior que zero");
        }

        PedidoVenda pedido = new PedidoVenda();
        pedido.setDescricao(descricao.trim());
        pedido.setClienteEmail(email.trim());
        pedido.setValor(valor);
        return repository.save(pedido);
    }

    public PedidoVenda fechar(Long id) {
        PedidoVenda pedido = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido nao encontrado"));

        // Evita reenviar uma venda que ja tem ID de fatura.
        if (pedido.getStatus() == StatusPedido.FATURADO && pedido.getFaturaId() != null) {
            return pedido;
        }

        try {
            Long faturaId = hub.faturar(pedido.getCodigoPedido(),
                    pedido.getClienteEmail(), pedido.getValor());
            pedido.setFaturaId(faturaId);
            pedido.setStatus(StatusPedido.FATURADO);
            pedido.setErroFaturamento(null);
            return repository.save(pedido);
        } catch (IllegalStateException e) {
            // Mantem o pedido cadastrado para tentar novamente.
            pedido.setStatus(StatusPedido.ERRO_FATURAMENTO);
            pedido.setErroFaturamento(e.getMessage());
            repository.save(pedido);
            throw e;
        }
    }
}
