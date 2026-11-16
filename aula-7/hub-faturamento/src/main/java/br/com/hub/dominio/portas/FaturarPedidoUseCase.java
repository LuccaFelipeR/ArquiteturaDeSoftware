package br.com.hub.dominio.portas;

import br.com.hub.dominio.Fatura;
import br.com.hub.dominio.Pedido;

public interface FaturarPedidoUseCase {
    Fatura faturar(Pedido pedido);
    Fatura consultar(Long id);
    void processarBoleto(Long id);
}
