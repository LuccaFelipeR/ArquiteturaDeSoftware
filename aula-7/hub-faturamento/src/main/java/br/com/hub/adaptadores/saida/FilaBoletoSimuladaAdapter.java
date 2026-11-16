package br.com.hub.adaptadores.saida;

import br.com.hub.dominio.portas.FilaBoletoPort;
import org.springframework.stereotype.Component;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class FilaBoletoSimuladaAdapter implements FilaBoletoPort {
    // Fila em memoria para a atividade. Nao exige RabbitMQ ou outro servidor.
    private final Queue<Long> mensagens = new ConcurrentLinkedQueue<>();

    @Override
    public void publicar(Long faturaId) {
        mensagens.add(faturaId);
        System.out.println("FILA SIMULADA: fatura " + faturaId + " aguardando boleto");
    }

    public Long receber() {
        return mensagens.poll();
    }
}
