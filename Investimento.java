public class Investimento {

    private Ativo ativo;                       // ativo comprado
    private double valorAplicado;              // valor investido pelo usuário
    private double valorInicial;               // valor do ativo no momento da compra
    private double lucroPrejuizoAcumulado;     // usado para saber quanto já lucrou/perdeu

    public Investimento(Ativo ativo, double valorAplicado, double valorInicial, double lucroPrejuizoAcumulado) {
        this.ativo = ativo;
        this.valorAplicado = valorAplicado;
        this.valorInicial = valorInicial;
        this.lucroPrejuizoAcumulado = lucroPrejuizoAcumulado;
    }

    // ----------------------
    //       GETTERS
    // ----------------------

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
        this.valorAplicado = valorAplicado;
    }

    public double getValorInicial() {
        return valorInicial;
    }

    public void setValorInicial(double valorInicial) {
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

    // Lucro/prejuízo atual calculado agora
    public double calcularLucroPrejuizoAtual() {
        return ativo.getValorAtual() - valorInicial;
    }

    // Atualiza o lucro acumulado
    public void atualizarLucroAcumulado() {
        this.lucroPrejuizoAcumulado += calcularLucroPrejuizoAtual();
    }

    @Override
    public String toString() {
        return "Investimento em " + ativo.getCodigo() +
               " | Valor Aplicado: R$ " + valorAplicado +
               " | Valor Inicial: R$ " + valorInicial +
               " | Lucro/Prejuizo acumulado: R$ " + lucroPrejuizoAcumulado;
    }
}
