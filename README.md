# Sistema de Transações Financeiras com Sockets Multithread

Este projeto implementa um sistema de transações financeiras usando sockets multithread em Java. O servidor recebe requisições de compra com cartões de crédito e débito, processa as transações com base no saldo disponível e responde com mensagens no formato ISO 8583, um protocolo amplamente utilizado em sistemas de pagamento eletrônico.

## Funcionalidades

O sistema simula um servidor e um cliente que trocam mensagens de transação financeira. O servidor valida as transações com base no saldo dos cartões armazenados em memória e envia respostas conforme o tipo de transação.

1. **Cartão de Crédito/Débito:** A classe `Cartao` representa um cartão bancário e oferece operações de débito e crédito.
2. **Testes de Transação:** A classe `Cliente` realiza diferentes testes de transação simulando situações de saldo insuficiente, cartão inexistente e transações aprovadas.
3. **Servidor de Transações:** O servidor aguarda conexões de clientes, processa transações e responde com um código de status e um número de autorização único (NSU).

## Estrutura do Projeto

- **Cartao.java:** Define a classe `Cartao` com informações do cartão e saldo, e métodos para debitar valores.
- **Cliente.java:** Simula o comportamento de um cliente enviando transações para o servidor.
- **Servidor.java:** Implementa o servidor que processa as transações, verificando a validade do cartão e o saldo.
- **Transacao.java:** Representa a transação, armazenando o valor, código de resposta e NSU.
  
## Testes Realizados

O cliente realiza os seguintes testes de transação:

1. **Saldo Insuficiente:** Tenta realizar uma transação com saldo insuficiente em um cartão.
2. **Cartão Inexistente:** Tenta realizar uma transação com um número de cartão inválido.
3. **Transação Aprovada:** Realiza uma transação com saldo suficiente e um cartão válido.

## Exemplo de Resposta

Ao realizar uma transação, o servidor retorna uma mensagem formatada com o código de resposta e o NSU. Exemplo:

```bash
Mensagem enviada: 0200120000000500000000000000123456789012345011122101203REDE123987654321098765400 
Resposta do servidor: 0210100000000500000000000000123456789012345011122101203REDE12300500000000000000001 
Código de resposta: 00 
NSU: 00000000000000000001
```

- **Código de resposta:**
  - `00`: Transação aprovada.
  - `05`: Cartão inexistente.
  - `51`: Saldo insuficiente.

## Considerações

- O servidor e o cliente se comunicam utilizando o protocolo de mensagens ISO 8583.
- O servidor aceita múltiplas conexões simultâneas, processando transações em threads separadas.
  
## Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais informações.