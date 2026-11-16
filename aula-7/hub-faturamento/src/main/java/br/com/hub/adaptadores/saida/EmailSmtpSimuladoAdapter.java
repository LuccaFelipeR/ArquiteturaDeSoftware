package br.com.hub.adaptadores.saida;

import br.com.hub.dominio.Fatura;
import br.com.hub.dominio.portas.EmailPort;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Component
public class EmailSmtpSimuladoAdapter implements EmailPort {
    @Override
    public String enviarEmail(Fatura fatura) {
        String conteudo = "Fatura " + fatura.id() + " do pedido " + fatura.pedidoId()
                + " no valor de R$ " + fatura.valor()
                + ". Nota fiscal: " + fatura.chaveAcessoNfe();
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256")
                    .digest(conteudo.getBytes(StandardCharsets.UTF_8));
            System.out.println("SMTP SIMULADO: email para " + fatura.clienteEmail());
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Nao foi possivel calcular o hash", e);
        }
    }
}
