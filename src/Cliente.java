import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {

    public static void main(String[] args) {
        System.out.println("Iniciando testes de transações...\n");
    
        // Teste 1: Saldo insuficiente (cartão válido, mas saldo insuficiente)
        executarTeste("401231021845", 500.0, "1", "123456", "1203");
    
        // Teste 2: Cartão inexistente
        executarTeste("0000000000000000", 100.0, "2", "123457", "1203");
    
        // Teste 3: Transação aprovada (cartão válido e saldo suficiente)
        executarTeste("9876543210987654", 50.0, "1", "123458", "1203");
    
        System.out.println("\nTestes concluídos.");
    }
    

    private static void executarTeste(String numeroCartao, double valor, String formaPagamento, String horario, String data) {
        try (Socket socket = new Socket("127.0.0.1", 5000)) { // Nova conexão para cada teste
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    
            System.out.println("Enviando transação:");
            System.out.println("Cartão: " + numeroCartao);
            System.out.println("Valor: R$" + valor);
            System.out.println("Forma de pagamento: " + (formaPagamento.equals("1") ? "Débito" : "Crédito"));
            System.out.println("Horário: " + horario);
            System.out.println("Data: " + data);
    
            String mensagem = criarMensagemISO8583(numeroCartao, valor, formaPagamento, horario, "REDE123", data);
            writer.println(mensagem);

            System.out.println("Mensagem enviada: " + mensagem);
    
            String resposta = reader.readLine();
            if (resposta != null) {
                System.out.println("Resposta do servidor: " + resposta);
                System.out.println("Código de resposta: " + resposta.substring(35, 37));
                System.out.println("NSU: " + resposta.substring(37, 57));
            } else {
                System.out.println("Nenhuma resposta do servidor recebida.");
            }
            System.out.println("--------------------------------------------------");
    
        } catch (IOException e) {
            System.err.println("Erro ao executar teste: " + e.getMessage());
        }
    }
    

    private static String criarMensagemISO8583(String numeroCartao, double valor, String formaPagamento, String horario, String redeTransmissora, String data) {
        String tipoTransacao = "0200";
        String valorEmCentavos = String.format("%012d", (int) (valor * 100));
        String redeTransmissoraFormatada = String.format("%-9s", redeTransmissora);
        String numeroCartaoFormatado = String.format("%-16s", numeroCartao);
        String formaPagamentoFormatada = String.format("%-1s", formaPagamento);
        String horaFormatada = String.format("%-6s", horario);
        String dataFormatada = String.format("%-4s", data);

        return tipoTransacao + valorEmCentavos + horaFormatada + dataFormatada + redeTransmissoraFormatada + numeroCartaoFormatado + formaPagamentoFormatada;
    }
}
