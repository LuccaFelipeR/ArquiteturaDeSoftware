package br.com.hub.adaptadores.entrada;

import br.com.hub.adaptadores.saida.FilaBoletoSimuladaAdapter;
import br.com.hub.dominio.portas.FaturarPedidoUseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ConsumidorFilaBoletoAdapter {
    private final FilaBoletoSimuladaAdapter fila;
    private final FaturarPedidoUseCase faturamento;

    public ConsumidorFilaBoletoAdapter(FilaBoletoSimuladaAdapter fila,
                                       FaturarPedidoUseCase faturamento) {
        this.fila = fila;
        this.faturamento = faturamento;
    }

    @Scheduled(fixedDelay = 300)
    public void lerFila() {
        Long id = fila.receber();
        if (id == null) {
            return;
        }
        try {
            faturamento.processarBoleto(id);
        } catch (RuntimeException e) {
            // Na simulacao, registramos falhas para nao interromper o consumidor.
            System.err.println("Erro ao gerar boleto da fatura " + id + ": " + e.getMessage());
        }
    }
}
