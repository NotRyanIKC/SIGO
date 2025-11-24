public class SistemaInvestidores {

    private DLL<Investidor> ListaInvestidores;

    public SistemaInvestidores() {
        this.ListaInvestidores = new DLL<>();
    }

    public void cadastrarInvestidor(Investidor investidor) {
        ListaInvestidores.add(investidor);
    }

    public void investir(Investidor inv, Ativo ativo, double valor) {
        // Lógica para processar o investimento
        
    }

    public void resgatar(Investidor inv, Ativo ativo, double valor) {
        // Lógica para processar o resgate
        
    }

    public DLL<Investidor> getListaInvestidores() {
        return ListaInvestidores;
    }

}
