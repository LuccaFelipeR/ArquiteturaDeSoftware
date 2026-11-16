# Aula 7 - Hub de Faturamento (Arquitetura Hexagonal)

Esta entrega parte do projeto MVC da Aula 6 (pasta `MVC`, preservada) e acrescenta
um hub independente, na pasta `hub-faturamento`, para receber os pedidos por REST.

## Requisitos implementados

- **RF01:** `POST /faturas` recebe pedido, cria fatura no H2 e retorna o ID.
- **RF02:** um adaptador publica o ID numa fila simulada em memoria; o consumidor
  processa a mensagem e salva um codigo de barras ficticio de 44 digitos na fatura.
- **RF03:** adaptador SOAP simulado gera chave de acesso numerica de 44 digitos.
- **RF04:** adaptador SMTP simulado gera o hash SHA-256 do conteudo da notificacao
  e salva o resultado na fatura.

**Nota:** fila, SOAP, SMTP, codigo de barras e chave fiscal sao simulacoes academicas.
Nao ha emissao de boleto ou NF-e real, nem envio de mensagem a um email externo.

## Estrutura hexagonal

- `dominio`: entidades e interfaces (portas), sem dependencia do Spring.
- `aplicacao`: caso de uso de faturamento que chama apenas as portas, sem Spring.
- `adaptadores/entrada`: REST e consumidor da fila de boletos.
- `adaptadores/saida`: JPA/H2, fila simulada, boleto, SOAP e SMTP simulados.

## Executar no Windows (CMD)

```cmd
cd /d "C:\Projetos\ArquiteturaDeSoftware\aula-7\hub-faturamento"
mvnw.cmd clean spring-boot:run
```

Aplicacao em `http://localhost:8081/status`.

## Teste via CMD

Deixe o hub aberto e use outro terminal CMD:

```cmd
curl -i -X POST http://localhost:8081/faturas -H "Content-Type: application/json" -d "{\"pedidoId\":\"PED-001\",\"clienteEmail\":\"cliente@teste.com\",\"valor\":150.00}"
```

Deve retornar **HTTP 201** e um JSON com `id`, `chaveAcessoNfe` e `hashEmail`.
Como o boleto passa pela fila simulada, o `codigoBarras` pode vir vazio
na primeira resposta. Em seguida consulte:

```cmd
curl http://localhost:8081/faturas/1
```

Na consulta deve aparecer `status: FATURADA`, codigo de barras de 44 digitos,
chave fiscal de 44 digitos e hash SHA-256 de 64 caracteres hexadecimais.

Os dados do banco H2 em memoria reiniciam quando a aplicacao e encerrada.

## Testes automatizados

```cmd
mvnw.cmd test
```

O MVC original desta pasta continua na porta 8080 e nao precisa estar ligado
para testar o hub da Aula 7. A integracao entre MVC e hub sera feita na Aula 8.
