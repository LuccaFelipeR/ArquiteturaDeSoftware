package br.com.hub.adaptadores.saida;

import br.com.hub.dominio.Fatura;
import br.com.hub.dominio.portas.NotaFiscalPort;
import org.springframework.stereotype.Component;
import java.math.BigInteger;
import java.util.UUID;

@Component
public class NotaFiscalSoapSimuladoAdapter implements NotaFiscalPort {
    @Override
    public String emitirNota(Fatura fatura) {
        // Simula a resposta do emissor SOAP, sem acessar um servico externo.
        String numero = new BigInteger(UUID.randomUUID().toString().replace("-", ""), 16)
                .toString();
        String chave = ("0".repeat(44) + numero);
        chave = chave.substring(chave.length() - 44);
        System.out.println("SOAP SIMULADO: nota da fatura " + fatura.id() + " chave " + chave);
        return chave;
    }
}
