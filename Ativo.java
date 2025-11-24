public class Ativo {
    private String codigo;
    private String nome;
    private String tipo;
    private String risco;
    private double rentabilidadeMedia;
    private double valorAtual;
    private double variacaoPercentual;
    private int ciclosNegativos;

    public Ativo(String codigo, String nome, String tipo, String risco, double rentabilidadeMedia, double valorAtual, double variacaoPercentual, int ciclosNegativos) {
        this.codigo = codigo;
        this.nome = nome;
        this.tipo = tipo;
        this.risco = risco;
        this.rentabilidadeMedia = rentabilidadeMedia;
        this.valorAtual = valorAtual;
        this.variacaoPercentual = variacaoPercentual;
        this.ciclosNegativos = 0;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getRisco() {
        return risco;
    }

    public void setRisco(String risco) {
        this.risco = risco;
    }

    public double getRentabilidadeMedia() {
        return rentabilidadeMedia;
    }

    public void setRentabilidadeMedia(double rentabilidadeMedia) {
        this.rentabilidadeMedia = rentabilidadeMedia;
    }

    public double getValorAtual() {
        return valorAtual;
    }

    public void setValorAtual(double valorAtual) {
        this.valorAtual = valorAtual;
    }

    public double getVariacaoPercentual() {
        return variacaoPercentual;
    }

    public void setVariacaoPercentual(double variacaoPercentual) {
        this.variacaoPercentual = variacaoPercentual;
    }

    public int getCiclosNegativos() {
        return ciclosNegativos;
    }

    public void setCiclosNegativos(int ciclosNegativos) {
        this.ciclosNegativos = ciclosNegativos;
    }


}
