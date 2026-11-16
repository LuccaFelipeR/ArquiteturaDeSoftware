package br.com.hub.adaptadores.saida;

import br.com.hub.dominio.Fatura;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "faturas")
public class FaturaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pedido_id", nullable = false, unique = true)
    private String pedidoId;

    private String clienteEmail;
    private BigDecimal valor;
    private String status;
    private String codigoBarras;
    private String chaveAcessoNfe;
    private String hashEmail;

    public FaturaEntity() {
    }

    public FaturaEntity(Fatura fatura) {
        id = fatura.id();
        pedidoId = fatura.pedidoId();
        clienteEmail = fatura.clienteEmail();
        valor = fatura.valor();
        status = fatura.status();
        codigoBarras = fatura.codigoBarras();
        chaveAcessoNfe = fatura.chaveAcessoNfe();
        hashEmail = fatura.hashEmail();
    }

    public Fatura paraDominio() {
        return new Fatura(id, pedidoId, clienteEmail, valor, status,
                codigoBarras, chaveAcessoNfe, hashEmail);
    }
}
