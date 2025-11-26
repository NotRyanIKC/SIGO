public class BuscaAtivos {

    public Ativo buscaLinear(DLL<Ativo> lista, String nome) {
        for (int i = 0; i < lista.size(); i++) { // Itera usando índice
            Ativo ativo = lista.get(i);
            if (ativo.getNome().equalsIgnoreCase(nome)) {
                return ativo;
            }
        }
        return null; // Retorna null se não encontrar
    }

    public Ativo buscaBinaria(DLL<Ativo> lista, String codigo) {
        int inicio = 0;
        int fim = lista.size() - 1;

        while (inicio <= fim) {
            int meio = (inicio + fim) / 2;
            Ativo ativoMeio = lista.get(meio); // Supondo que DLL tem método get(index)

            int comparacao = ativoMeio.getCodigo().compareTo(codigo);
            if (comparacao == 0) {
                return ativoMeio;
            } else if (comparacao < 0) {
                inicio = meio + 1;
            } else {
                fim = meio - 1;
            }
        }
        return null; // Retorna null se não encontrar
    }

}
