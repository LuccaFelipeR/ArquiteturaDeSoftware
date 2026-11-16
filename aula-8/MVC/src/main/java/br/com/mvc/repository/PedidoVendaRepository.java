package br.com.mvc.repository;

import br.com.mvc.model.PedidoVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PedidoVendaRepository extends JpaRepository<PedidoVenda, Long> {
    List<PedidoVenda> findAllByOrderByIdDesc();
}
