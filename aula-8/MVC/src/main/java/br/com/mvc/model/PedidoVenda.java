package br.com.mvc.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "pedidos_venda")
public class PedidoVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String descricao;

    @Column(nullable = false, length = 180)
    private String clienteEmail;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido status = StatusPedido.ABERTO;

    private Long faturaId;

    @Column(length = 500)
    private String erroFaturamento;

    public PedidoVenda() {
    }

    public String getCodigoPedido() {
        return id == null ? "" : "PED-" + id;
    }

    public Long getId() { return id; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getClienteEmail() { return clienteEmail; }
    public void setClienteEmail(String clienteEmail) { this.clienteEmail = clienteEmail; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public StatusPedido getStatus() { return status; }
    public void setStatus(StatusPedido status) { this.status = status; }
    public Long getFaturaId() { return faturaId; }
    public void setFaturaId(Long faturaId) { this.faturaId = faturaId; }
    public String getErroFaturamento() { return erroFaturamento; }
    public void setErroFaturamento(String erroFaturamento) { this.erroFaturamento = erroFaturamento; }
}
