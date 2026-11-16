package br.com.hub;

import br.com.hub.dominio.Fatura;
import br.com.hub.dominio.Pedido;
import br.com.hub.dominio.portas.FaturarPedidoUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FaturamentoIntegracaoTests {
    @Autowired
    FaturarPedidoUseCase faturamento;

    @Test
    void criaFaturaComNotaEmailEBoleto() throws InterruptedException {
        Pedido pedido = new Pedido("TESTE-" + UUID.randomUUID(), "cliente@teste.com",
                new BigDecimal("150.00"));

        Fatura criada = faturamento.faturar(pedido);
        assertNotNull(criada.id());
        assertTrue(criada.chaveAcessoNfe().matches("[0-9]{44}"));
        assertTrue(criada.hashEmail().matches("[0-9a-f]{64}"));

        Fatura atual = criada;
        for (int tentativas = 0; tentativas < 50; tentativas++) {
            atual = faturamento.consultar(criada.id());
            if (atual.codigoBarras() != null) break;
            Thread.sleep(100);
        }
        assertNotNull(atual.codigoBarras(), "O consumidor da fila deveria gerar o boleto");
        assertTrue(atual.codigoBarras().matches("[0-9]{44}"));
        assertEquals("FATURADA", atual.status());

        Fatura repetida = faturamento.faturar(pedido);
        assertEquals(criada.id(), repetida.id(), "O mesmo pedido nao deve criar outra fatura");
    }

    @Test
    void rejeitaValorNegativo() {
        assertThrows(IllegalArgumentException.class, () -> faturamento.faturar(
                new Pedido("TESTE-ERRO", "cliente@teste.com", new BigDecimal("-1"))));
    }
}
