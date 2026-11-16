package br.com.hub.dominio;

import java.math.BigDecimal;

// O dominio nao depende de banco, REST, fila, SOAP ou SMTP.
public record Fatura(
        Long id,
        String pedidoId,
        String clienteEmail,
        BigDecimal valor,
        String status,
        String codigoBarras,
        String chaveAcessoNfe,
        String hashEmail
) {
    public Fatura comId(Long novoId) {
        return new Fatura(novoId, pedidoId, clienteEmail, valor, status,
                codigoBarras, chaveAcessoNfe, hashEmail);
    }

    public Fatura comNotaFiscal(String chave) {
        return new Fatura(id, pedidoId, clienteEmail, valor, status,
                codigoBarras, chave, hashEmail);
    }

    public Fatura comEmail(String hash) {
        return new Fatura(id, pedidoId, clienteEmail, valor, status,
                codigoBarras, chaveAcessoNfe, hash);
    }

    public Fatura comBoleto(String codigo) {
        return new Fatura(id, pedidoId, clienteEmail, valor, status,
                codigo, chaveAcessoNfe, hashEmail);
    }

    public Fatura comStatus(String novoStatus) {
        return new Fatura(id, pedidoId, clienteEmail, valor, novoStatus,
                codigoBarras, chaveAcessoNfe, hashEmail);
    }
}
