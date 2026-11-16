package br.com.mvc.integracao;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class HubFaturamentoClient {

    private final RestClient client;

    public HubFaturamentoClient(@Value("${hub.url}") String urlHub) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(2000);
        factory.setReadTimeout(5000);
        this.client = RestClient.builder()
                .baseUrl(urlHub)
                .requestFactory(factory)
                .build();
    }

    public Long faturar(String pedidoId, String clienteEmail, BigDecimal valor) {
        try {
            PedidoRequest pedido = new PedidoRequest(pedidoId, clienteEmail, valor);
            FaturaResponse resposta = client.post()
                    .uri("/faturas")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(pedido)
                    .retrieve()
                    .body(FaturaResponse.class);

            if (resposta == null || resposta.id() == null) {
                throw new IllegalStateException("O hub nao retornou o ID da fatura");
            }
            return resposta.id();
        } catch (RestClientException e) {
            throw new IllegalStateException(
                    "Nao foi possivel acessar o hub. Confira se a Aula 7 esta ligada na porta 8081.", e);
        }
    }

    // Os nomes devem ser iguais aos recebidos pelo hub da Aula 7.
    public record PedidoRequest(String pedidoId, String clienteEmail, BigDecimal valor) {
    }

    public record FaturaResponse(Long id, String status) {
    }
}
