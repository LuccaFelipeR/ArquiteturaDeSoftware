# Aula 8 — Evolução do MVC e integração com o hub

Esta pasta é uma **continuação do projeto da Aula 6**. Mantive o controle de estoque
em `/produtos` e acrescentei o módulo de vendas em `/vendas`, usando os mesmos
pacotes `br.com.mvc`, Spring Boot 4.1.1, Thymeleaf, Spring Data JPA e H2.

O hub de faturamento **não foi duplicado nesta pasta**. Ele está na entrega da Aula 7,
no projeto `aula-7/hub-faturamento`, e deve estar ligado na porta 8081.

## Como executar no Windows / VS Code (CMD)

Abra dois terminais diferentes, a partir da raiz do repositório.

**Terminal 1 — Aula 7 (hub):**

```cmd
cd aula-7\hub-faturamento
mvnw.cmd clean spring-boot:run
```

O hub fica disponível em `http://localhost:8081/status`.

**Terminal 2 — Aula 8 (MVC):**

```cmd
cd aula-8\MVC
mvnw.cmd clean spring-boot:run
```

Abra **http://localhost:8080/vendas**.

## Demonstração passo a passo

1. Na tela de vendas, preencha a descrição, o e-mail e o valor. Clique em **Cadastrar pedido**.
2. O pedido aparece na tabela com código `PED-1` e status `ABERTO`.
3. Clique em **Fechar pedido**. O MVC envia um POST REST ao hub na porta 8081.
4. A tabela muda para **Faturado** e exibe o **ID da fatura** devolvido pelo hub.
5. Consulte `http://localhost:8081/faturas/1` (ajuste o número conforme o ID exibido).
6. Clique em **Ver estoque (Aula 6)** para comprovar que o cadastro e a movimentação de produtos foram preservados.

Para observar o tratamento de falhas, pare o hub (Ctrl+C), cadastre outra venda e tente
fechá-la. O pedido permanece na tabela e indica **Falha no hub**. Reinicie o hub e
clique em **Tentar novamente**.

> O H2 usa banco em memória para demonstração: ao reiniciar as aplicações, os registros
> cadastrados são perdidos. As operações de estoque não são modificadas automaticamente
> quando uma venda é faturada porque isso não foi solicitado no enunciado.

## Arquivos principais adicionados ao projeto MVC

- `model/PedidoVenda.java` e `model/StatusPedido.java`: dados e estados de uma venda
- `repository/PedidoVendaRepository.java`: persistência local
- `service/VendaService.java`: cadastro, fechamento e associação do ID da fatura
- `integracao/HubFaturamentoClient.java`: chamada REST, timeout e DTOs compatíveis com a Aula 7
- `controller/VendaController.java`: rotas de telas e formulários
- `resources/view/vendas.html`: interface Thymeleaf
- `analise-integracao.md`: dois problemas e soluções pedidos pelo professor

## Teste por terminal (opcional)

Com o MVC e o hub funcionando:

```cmd
curl -i http://localhost:8080/vendas
curl -i http://localhost:8081/status
```

A requisição real do POST ao hub é feita pelo MVC quando se clica em **Fechar pedido**.

## Entrega no GitHub

```cmd
cd /d "C:\Projetos\ArquiteturaDeSoftware"
git add aula-8
git diff --cached --stat
git commit -m "Aula 8 - Integra sistema MVC de vendas ao hub de faturamento"
git push origin main
```

Não execute `git add .` se houver outros arquivos modificados no repositório.
