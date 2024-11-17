public class Cartao {
   private String numero;
   private String nomeCliente;
   private double saldo;

   public Cartao(String numero, String nomeCliente, double saldo) {
       this.numero = numero;
       this.nomeCliente = nomeCliente;
       this.saldo = saldo;
   }

   public String getNumero() {
       return this.numero;
   }

   public String getNomeCliente() {
       return this.nomeCliente;
   }

   public double getSaldo() {
       return this.saldo;
   }

   public boolean debitar(double valor) {
      if (valor < 0) {
          System.out.println("Erro: valor negativo não permitido para débito.");
          return false;
      }
      if (this.saldo < valor) {
          System.out.println("Erro: saldo insuficiente.");
          return false;
      }
      this.saldo -= valor;
      return true;
  }
  
}