package br.com.hub.dominio.portas;

import br.com.hub.dominio.Fatura;
import java.util.Optional;

public interface FaturaRepositoryPort {
    Fatura salvar(Fatura fatura);
    Optional<Fatura> buscar(Long id);
    Optional<Fatura> buscarPorPedidoId(String pedidoId);
}
