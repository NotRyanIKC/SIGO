public class Transacao {
    private Investidor investidor;
    private Ativo ativo;
    private double valor;
    private String tipo;
    private String data;
    private String descricao;

    public Transacao(Investidor investidor, Ativo ativo, double valor, String tipo, String data) {
        this.investidor = investidor;
        this.ativo = ativo;
        this.valor = valor;
        this.tipo = tipo;
        this.data = data;
        String nomeAtivo = ativo != null ? ativo.getCodigo() : "";
        String nomeInvestidor = investidor != null ? investidor.getNome() : "";
        this.descricao = tipo + " - " + nomeAtivo + " - " + nomeInvestidor;
    }

    public Transacao(String descricao, double valor) {
        this.descricao = descricao;
        this.valor = valor;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
