public class Transacao {

    private Investidor investidor;
    private Ativo ativo;
    private double valor;
    private String tipo;
    private String data;


    public Transacao(Investidor investidor, Ativo ativo, double valor, String tipo, String data) {
        this.investidor = investidor;
        this.ativo = ativo;
        this.valor = valor;
        this.tipo = tipo;
        this.data = data;
    }   

    public Investidor getInvestidor() {
        return investidor;
    }

    public void setInvestidor(Investidor investidor) {
        this.investidor = investidor;
    }

    public Ativo getAtivo() {
        return ativo;
    }

    public void setAtivo(Ativo ativo) {
        this.ativo = ativo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}