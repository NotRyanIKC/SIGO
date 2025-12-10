public class Ativo {
    private String codigo;
    private String nome;
    private String tipo;
    private String risco;
    private double rentabilidadeMedia;
    private double valorAtual;
    private double variacaoPercentual;
    private int ciclosNegativos;

    public Ativo(String codigo, String nome, String tipo, String risco,
                 double rentabilidadeMedia, double valorAtual,
                 double variacaoPercentual, int ciclosNegativos) {
        this.codigo = codigo;
        this.nome = nome;
        this.tipo = tipo;
        this.risco = risco;
        this.rentabilidadeMedia = rentabilidadeMedia;
        this.valorAtual = valorAtual;
        this.variacaoPercentual = variacaoPercentual;
        this.ciclosNegativos = ciclosNegativos;
    }

    public String getCodigo() { return codigo; }

    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }

    public String getTipo() { return tipo; }

    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getRisco() { return risco; }

    public void setRisco(String risco) { this.risco = risco; }

    public double getRentabilidadeMedia() { return rentabilidadeMedia; }

    public boolean setRentabilidadeMedia(double rentabilidadeMedia) {
        if (rentabilidadeMedia < 0) {
            System.out.println("A rentabilidade media nao pode ser negativa.");
            return false;
        }
        this.rentabilidadeMedia = rentabilidadeMedia;
        return true;
    }

    public double getValorAtual() { return valorAtual; }

    public boolean setValorAtual(double valorAtual) {
        if (valorAtual < 0) {
            System.out.println("O valor atual nao pode ser negativo.");
            return false;
        }
        this.valorAtual = valorAtual;
        return true;
    }

    public double getVariacaoPercentual() { return variacaoPercentual; }

    public boolean setVariacaoPercentual(double variacaoPercentual) {
        this.variacaoPercentual = variacaoPercentual;
        return true;
    }

    public int getCiclosNegativos() { return ciclosNegativos; }

    public void setCiclosNegativos(int ciclosNegativos) {
        this.ciclosNegativos = ciclosNegativos;
    }

    public void incrementarCiclosNegativos() {
        this.ciclosNegativos++;
    }

    public void resetarCiclosNegativos() {
        this.ciclosNegativos = 0;
    }

    public void aplicarVariacaoPercentual(double deltaPercentual) {
        double novoValor = this.valorAtual * (1.0 + deltaPercentual / 100.0);
        if (novoValor < 0) {
            novoValor = 0.0;
        }
        this.valorAtual = novoValor;
        this.variacaoPercentual = deltaPercentual;

        if (deltaPercentual < 0) {
            incrementarCiclosNegativos();
        } else if (deltaPercentual > 0) {
            resetarCiclosNegativos();
        }
        // se delta = 0, mantém o contador como está
    }
}
