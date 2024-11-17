import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Servidor {

    private static Map<String, Cartao> cartoes = new HashMap<>(); 
    private static long contadorNSU = 1; 
    private static List<Transacao> transacoes = new ArrayList<>(); // Lista para armazenar transações

    public static void main(String[] args) {


        cartoes.put("401231021845", new Cartao("401231021845", "João", 100.00)); 
        cartoes.put("123456789012", new Cartao("123456789012", "Maria", 50.00));
        cartoes.put("9876543210987654", new Cartao("9876543210987654", "Jose", 200.00));


        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Servidor aguardando conexoes...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Cliente conectado!");

                new Thread(() -> processarTransacao(socket)).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
        private static void processarTransacao(Socket socket) {
    
            Transacao transacao = new Transacao();
    
            try (socket){
    
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String mensagem = reader.readLine();
                System.out.println("Mensagem recebida: " + mensagem);
                System.out.println("Tamanho da mensagem: " + mensagem.length());
        
                if (mensagem.length() != 52) {
                    System.out.println("Mensagem incompleta ou incorreta!");
                    return;
                }
        
                String numeroCartao = mensagem.substring(35, 51).trim(); 
                String valorEmCentavosStr = mensagem.substring(4, 16);
                double valorTransacao = Double.parseDouble(valorEmCentavosStr) / 100.0;
                String formaPagamento = mensagem.substring(51).trim(); 
                String horario = mensagem.substring(16, 22);
                String data = mensagem.substring(22, 26);
        
                String codigoResposta;
                String nsu;
        
                synchronized (cartoes) {
                    Cartao cartao = cartoes.get(numeroCartao);
        
                    if (cartao == null) {
                        codigoResposta = "05";
                        nsu = "00000000000000000000";
                    } else if (cartao.getSaldo() < valorTransacao) {
                        codigoResposta = "51";
                        nsu = "00000000000000000000";
                    } else {
                        cartao.debitar(valorTransacao);
                        codigoResposta = "00";
                        nsu = String.format("%020d", contadorNSU++);
                    }

                    System.out.println("Codigo de resposta: " + codigoResposta);
                    
                    System.out.println("NSU: " + nsu);
                    transacao.setCodigoResposta(codigoResposta);
                    transacao.setNSU(nsu);
                    
                    transacoes.add(transacao);
                }
    
                String tipoMensagemResp = "0210";
    
                String resposta = tipoMensagemResp + valorEmCentavosStr + horario + data + transacao.getRedeTransmissora() + codigoResposta + nsu;
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);
                writer.println(resposta);
    
            } catch (IOException ex) {
                ex.printStackTrace();
            }
    }
}