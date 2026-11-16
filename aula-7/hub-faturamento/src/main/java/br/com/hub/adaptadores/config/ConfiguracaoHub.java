package br.com.hub.adaptadores.config;

import br.com.hub.aplicacao.FaturamentoService;
import br.com.hub.dominio.portas.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfiguracaoHub {
    @Bean
    public FaturarPedidoUseCase faturarPedidoUseCase(FaturaRepositoryPort repositorio,
                                                     FilaBoletoPort fila,
                                                     BoletoPort boleto,
                                                     NotaFiscalPort nota,
                                                     EmailPort email) {
        return new FaturamentoService(repositorio, fila, boleto, nota, email);
    }
}
