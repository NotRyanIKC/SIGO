public class SistemaRecomendacoes {

    private DLL<Recomendacao> ListaRecomendacoes;

    public SistemaRecomendacoes() {
        this.ListaRecomendacoes = new DLL<>();
    }

    public void adicionarRecomendacao(Recomendacao recomendacao) {
        ListaRecomendacoes.add(recomendacao);
    }

    public DLL<Recomendacao> getListaRecomendacoes() {
        return ListaRecomendacoes;
    }

}