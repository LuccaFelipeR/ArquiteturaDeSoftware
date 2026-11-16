package br.com.hub.aplicacao;

import br.com.hub.dominio.Fatura;
import br.com.hub.dominio.Pedido;
import br.com.hub.dominio.portas.*;
import java.util.NoSuchElementException;

public class FaturamentoService implements FaturarPedidoUseCase {

    private final FaturaRepositoryPort repositorio;
    private final FilaBoletoPort filaBoleto;
    private final BoletoPort emissorBoleto;
    private final NotaFiscalPort emissorNotaFiscal;
    private final EmailPort notificacaoEmail;

    public FaturamentoService(FaturaRepositoryPort repositorio, FilaBoletoPort filaBoleto,
                              BoletoPort emissorBoleto, NotaFiscalPort emissorNotaFiscal,
                              EmailPort notificacaoEmail) {
        this.repositorio = repositorio;
        this.filaBoleto = filaBoleto;
        this.emissorBoleto = emissorBoleto;
        this.emissorNotaFiscal = emissorNotaFiscal;
        this.notificacaoEmail = notificacaoEmail;
    }

    @Override
    public Fatura faturar(Pedido pedido) {
        validar(pedido);
        String pedidoId = pedido.pedidoId().trim();

        // Impede gerar uma segunda fatura para o mesmo pedido.
        var existente = repositorio.buscarPorPedidoId(pedidoId);
        if (existente.isPresent()) {
            return existente.get();
        }

        Fatura fatura = new Fatura(null, pedidoId, pedido.clienteEmail().trim(),
                pedido.valor(), "CRIADA", null, null, null);
        fatura = repositorio.salvar(fatura); // RF01: cria e persiste

        String chave = emissorNotaFiscal.emitirNota(fatura); // RF03: SOAP simulado
        fatura = fatura.comNotaFiscal(chave);

        String hash = notificacaoEmail.enviarEmail(fatura); // RF04: SMTP simulado
        fatura = repositorio.salvar(fatura.comEmail(hash).comStatus("AGUARDANDO_BOLETO"));

        filaBoleto.publicar(fatura.id()); // RF02: mensagem na fila simulada
        return fatura;
    }

    @Override
    public Fatura consultar(Long id) {
        return repositorio.buscar(id)
                .orElseThrow(() -> new NoSuchElementException("Fatura nao encontrada"));
    }

    @Override
    public void processarBoleto(Long id) {
        Fatura fatura = consultar(id);
        if (fatura.codigoBarras() != null) {
            return; // Uma segunda entrega da fila nao gera outro boleto.
        }
        String codigo = emissorBoleto.gerarCodigoBarras(fatura);
        repositorio.salvar(fatura.comBoleto(codigo).comStatus("FATURADA"));
    }

    private void validar(Pedido pedido) {
        if (pedido == null || pedido.pedidoId() == null || pedido.pedidoId().isBlank()) {
            throw new IllegalArgumentException("Informe o ID do pedido");
        }
        if (pedido.clienteEmail() == null || !pedido.clienteEmail().contains("@")) {
            throw new IllegalArgumentException("Informe um email valido");
        }
        if (pedido.valor() == null || pedido.valor().signum() <= 0) {
            throw new IllegalArgumentException("O valor deve ser maior que zero");
        }
    }
}
