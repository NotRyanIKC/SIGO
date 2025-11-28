public class Investimento {
    private Ativo ativo;
    private double valorAplicado;
    private double valorInicial;
    private double lucroPrejuizoAcumulado;

    public Investimento(Ativo ativo, double valorAplicado, double valorInicial, double lucroPrejuizoAcumulado) {
        this.ativo = ativo;
        this.valorAplicado = valorAplicado;
        this.valorInicial = valorInicial;
        this.lucroPrejuizoAcumulado = lucroPrejuizoAcumulado;
    }

    public Ativo getAtivo() {
        return ativo;
    }

    public void setAtivo(Ativo ativo) {
        this.ativo = ativo;
    }

    public double getValorAplicado() {
        return valorAplicado;
    }

    public void setValorAplicado(double valorAplicado) {
        if (valorAplicado < 0) {
            throw new IllegalArgumentException("O valor aplicado não pode ser negativo.");
        }
        this.valorAplicado = valorAplicado;
    }

    public double getValorInicial() {
        return valorInicial;
    }

    public void setValorInicial(double valorInicial) {
        if (valorInicial < 0) {
            throw new IllegalArgumentException("O valor inicial não pode ser negativo.");
        }
        this.valorInicial = valorInicial;
    }

    public double getLucroPrejuizoAcumulado() {
        return lucroPrejuizoAcumulado;
    }

    public void setLucroPrejuizoAcumulado(double lucroPrejuizoAcumulado) {
        this.lucroPrejuizoAcumulado = lucroPrejuizoAcumulado;
    }

    public double getValorAtual() {
        return ativo.getValorAtual();
    }

    public double calcularLucroPrejuizoAtual() {
        return ativo.getValorAtual() - valorInicial;
    }

    public void atualizarLucroAcumulado() {
        this.lucroPrejuizoAcumulado += calcularLucroPrejuizoAtual();
    }

    public String toString() {
        return "Investimento em " + ativo.getCodigo()
                + " | Valor Aplicado: R$ " + valorAplicado
                + " | Valor Inicial: R$ " + valorInicial
                + " | Lucro/Prejuizo acumulado: R$ " + lucroPrejuizoAcumulado;
    }
}
