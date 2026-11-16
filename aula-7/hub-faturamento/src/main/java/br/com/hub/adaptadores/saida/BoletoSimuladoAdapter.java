package br.com.hub.adaptadores.saida;

import br.com.hub.dominio.Fatura;
import br.com.hub.dominio.portas.BoletoPort;
import org.springframework.stereotype.Component;
import java.math.BigInteger;
import java.util.UUID;

@Component
public class BoletoSimuladoAdapter implements BoletoPort {
    @Override
    public String gerarCodigoBarras(Fatura fatura) {
        // Codigo ficticio de 44 digitos, apenas para demonstrar a emissao.
        String numero = new BigInteger(UUID.randomUUID().toString().replace("-", ""), 16)
                .toString();
        String codigo = ("0".repeat(44) + numero);
        codigo = codigo.substring(codigo.length() - 44);
        System.out.println("BOLETO SIMULADO: fatura " + fatura.id() + " codigo " + codigo);
        return codigo;
    }
}
