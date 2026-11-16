package br.com.hub.adaptadores.saida;

import br.com.hub.dominio.Fatura;
import br.com.hub.dominio.portas.FaturaRepositoryPort;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class FaturaPersistenciaAdapter implements FaturaRepositoryPort {
    private final FaturaJpaRepository jpa;

    public FaturaPersistenciaAdapter(FaturaJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Fatura salvar(Fatura fatura) {
        return jpa.save(new FaturaEntity(fatura)).paraDominio();
    }

    @Override
    public Optional<Fatura> buscar(Long id) {
        return jpa.findById(id).map(FaturaEntity::paraDominio);
    }

    @Override
    public Optional<Fatura> buscarPorPedidoId(String pedidoId) {
        return jpa.findByPedidoId(pedidoId).map(FaturaEntity::paraDominio);
    }
}
