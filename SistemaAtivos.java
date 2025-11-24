public class SistemaAtivos {

    private DLL<Ativo> ListaAtivos;

    public SistemaAtivos() {
        this.ListaAtivos = new DLL<>();
    }

    public void cadastrarAtivo(Ativo ativo) {
        ListaAtivos.add(ativo);
    }


    public DLL<Ativo> getListaAtivos() {
        return ListaAtivos;
    }

    public void setListaAtivos(DLL<Ativo> ListaAtivos) {
        this.ListaAtivos = ListaAtivos;
    }

    


}
