package br.com.hub.adaptadores.saida;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FaturaJpaRepository extends JpaRepository<FaturaEntity, Long> {
    Optional<FaturaEntity> findByPedidoId(String pedidoId);
}
