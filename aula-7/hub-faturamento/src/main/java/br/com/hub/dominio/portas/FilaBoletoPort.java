package br.com.hub.dominio.portas;

public interface FilaBoletoPort {
    void publicar(Long faturaId);
}
