package br.com.hub.dominio;

import java.math.BigDecimal;

public record Pedido(String pedidoId, String clienteEmail, BigDecimal valor) {
}
