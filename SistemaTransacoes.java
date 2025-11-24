public class SistemaTransacoes {

    private DLL<Transacao> ListaTransacoes;

    public SistemaTransacoes() {
        this.ListaTransacoes = new DLL<>();
    }

    public void registrar(Transacao t) {
        ListaTransacoes.add(t);
    }

    public DLL<Transacao> getListaTransacoes() {
        return ListaTransacoes;
    }

}
