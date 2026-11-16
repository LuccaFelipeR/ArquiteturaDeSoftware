package br.com.mvc.service;

import br.com.mvc.model.Produto;
import br.com.mvc.model.StatusProduto;
import br.com.mvc.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public List<Produto> listar() {
        return repository.findAll();
    }

    public Produto buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
    }

    public Produto cadastrar(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Informe o nome do produto");
        }

        Produto produto = new Produto();
        produto.setNome(nome.trim());
        return repository.save(produto);
    }

    @Transactional
    public Produto entrada(Long id, int quantidade) {
        validarQuantidade(quantidade);

        Produto produto = buscar(id);
        verificarStatus(produto);

        produto.setQuantidade(produto.getQuantidade() + quantidade);
        produto.setStatus(StatusProduto.DISPONIVEL);

        return repository.save(produto);
    }

    @Transactional
    public Produto saida(Long id, int quantidade) {
        validarQuantidade(quantidade);

        Produto produto = buscar(id);
        verificarStatus(produto);

        if (quantidade > produto.getQuantidade()) {
            throw new IllegalArgumentException("Estoque insuficiente");
        }

        int saldo = produto.getQuantidade() - quantidade;
        produto.setQuantidade(saldo);

        if (saldo == 0) {
            produto.setStatus(StatusProduto.INDISPONIVEL);
        }

        return repository.save(produto);
    }

    @Transactional
    public Produto descontinuar(Long id, boolean administrador) {
        if (!administrador) {
            throw new IllegalStateException("Apenas o ADM pode descontinuar produtos");
        }

        Produto produto = buscar(id);
        produto.setStatus(StatusProduto.DESCONTINUADO);

        return repository.save(produto);
    }

    private void validarQuantidade(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero");
        }
    }

    private void verificarStatus(Produto produto) {
        if (produto.getStatus() == StatusProduto.DESCONTINUADO) {
            throw new IllegalStateException("Produto descontinuado");
        }
    }
}
