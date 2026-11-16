package br.com.hub.dominio.portas;

import br.com.hub.dominio.Fatura;

public interface BoletoPort {
    String gerarCodigoBarras(Fatura fatura);
}
