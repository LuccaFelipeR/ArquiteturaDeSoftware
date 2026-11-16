package br.com.hub.dominio.portas;

import br.com.hub.dominio.Fatura;

public interface EmailPort {
    String enviarEmail(Fatura fatura);
}
