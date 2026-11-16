# Aula 8 — Análise da integração MVC e Hub de Faturamento

## O que foi implementado

A aplicação MVC da Aula 6 foi aproveitada e ganhou a tela **Vendas** (`/vendas`).
Ela cadastra um pedido no banco H2 e, quando o usuário clica em **Fechar pedido**,
chama `POST http://localhost:8081/faturas` no hub hexagonal da Aula 7.
O corpo tem `pedidoId`, `clienteEmail` e `valor`. O hub devolve a fatura com um `id`.
O MVC salva esse ID em `PedidoVenda.faturaId` e muda o status para `FATURADO`.

O módulo de estoque anterior foi mantido em `/produtos`. O enunciado pede integração
de vendas e faturamento; não determina que fechar pedido dê baixa automática no estoque,
por isso essa funcionalidade não foi acrescentada.

## Problema 1 — O hub demora ou fica fora do ar

**Situação:** o vendedor clica em Fechar pedido, mas a comunicação com o hub atrasa
ou o serviço da porta 8081 está desligado. Como o MVC precisa do ID da fatura,
o fechamento da venda fica esperando e pode falhar.

**Impacto no pedido:** no exemplo implementado, o pedido continua cadastrado e recebe
o status `ERRO_FATURAMENTO`, sem ID de fatura. O usuário pode tentar novamente.
Se o hub tiver criado a fatura antes do erro de comunicação, é possível que a resposta
não tenha chegado ao MVC.

**Impacto no negócio:** o vendedor não consegue confirmar o faturamento, o cliente
fica sem confirmação e a operação perde tempo com novas tentativas.

**Solução estudada — timeout:** o cliente REST da aplicação MVC usa um limite de
2 segundos para conexão e 5 segundos para leitura. Assim a tela não fica esperando
indefinidamente e mostra uma mensagem de falha para o usuário.
O timeout age **na chamada REST entre o MVC e o hub**. Ele não recupera o serviço
e nem garante que a primeira chamada não tenha sido processada.

Um **circuit breaker** seria uma evolução interessante se o hub falhasse repetidamente:
após um número de erros, o MVC deixaria de enviar chamadas por um período.
Não foi implementado neste exercício.

## Problema 2 — O mesmo pedido chega duas vezes

**Situação:** o hub recebe o fechamento, grava a fatura, mas a resposta de rede se perde.
O vendedor clica em Tentar novamente, enviando o mesmo identificador `PED-<id>`.
Sem proteção, o hub poderia emitir duas faturas ou dois boletos para uma só venda.

**Impacto no pedido:** o MVC poderia associar um segundo ID à mesma venda, gerando
duplicidade no faturamento.

**Impacto no negócio:** o cliente poderia receber cobranças duplicadas e o setor
financeiro teria de fazer correções ou cancelamentos.

**Solução estudada — idempotência:** o MVC sempre usa o mesmo identificador para
uma venda (`PED-` + ID). O hub da Aula 7 consulta se já existe uma fatura para esse
`pedidoId` antes de criar outra; se já existe, devolve a mesma fatura.
Essa proteção age **no recebimento do pedido pelo hub** e torna o reenvio mais seguro.
A implementação acadêmica é simplificada; em produção, também seriam necessários
índice único no banco e tratamento de chamadas simultâneas.

## Conclusão

A Aula 8 demonstra dois processos separados que conversam por REST. O MVC controla
os pedidos e a interface; o hub concentra o faturamento e suas integrações simuladas.
A rede introduz atrasos e repetição, por isso timeout e idempotência são importantes.
