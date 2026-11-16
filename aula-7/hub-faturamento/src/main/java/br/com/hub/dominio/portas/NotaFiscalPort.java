package br.com.hub.dominio.portas;

import br.com.hub.dominio.Fatura;

public interface NotaFiscalPort {
    String emitirNota(Fatura fatura);
}
