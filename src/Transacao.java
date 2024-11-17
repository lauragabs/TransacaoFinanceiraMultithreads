public class Transacao {
    private double valor;
    private String data;
    private String hora;
    private String redeTransmissora;
    private String formaPagamento;
    private String codigoResposta;
    private String NSU;


    public String getRedeTransmissora() {
        return redeTransmissora = "104460512";
    }


    public void setCodigoResposta(String codigoResposta) {
        this.codigoResposta = codigoResposta;
    }


    public void setNSU(String nSU) {
        NSU = nSU;
    }
    
}