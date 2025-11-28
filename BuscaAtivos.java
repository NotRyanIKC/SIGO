public class BuscaAtivos {
    public Ativo buscaLinear(DLL<Ativo> lista, String nome) {
        long inicio = System.nanoTime();
        int comparacoes = 0;

        for (int i = 0; i < lista.size(); i++) {
            comparacoes++;
            Ativo ativo = lista.get(i);
            if (ativo.getNome().equalsIgnoreCase(nome)) {
                long fim = System.nanoTime();
                System.out.printf("Busca linear concluída em %d ns com %d comparações.%n", (fim - inicio), comparacoes);
                return ativo;
            }
        }

        long fim = System.nanoTime();
        System.out.printf("Busca linear concluída em %d ns com %d comparações.%n", (fim - inicio), comparacoes);
        return null;
    }

    public Ativo buscaBinaria(DLL<Ativo> lista, String codigo) {
        long inicio = System.nanoTime();
        int comparacoes = 0;

        int inicioIdx = 0;
        int fimIdx = lista.size() - 1;

        while (inicioIdx <= fimIdx) {
            comparacoes++;
            int meio = (inicioIdx + fimIdx) / 2;
            Ativo ativoMeio = lista.get(meio);
            int comparacao = ativoMeio.getCodigo().compareTo(codigo);

            if (comparacao == 0) {
                long fim = System.nanoTime();
                System.out.printf("Busca binária concluída em %d ns com %d comparações.%n", (fim - inicio), comparacoes);
                return ativoMeio;
            } else if (comparacao < 0) {
                inicioIdx = meio + 1;
            } else {
                fimIdx = meio - 1;
            }
        }

        long fim = System.nanoTime();
        System.out.printf("Busca binária concluída em %d ns com %d comparações.%n", (fim - inicio), comparacoes);
        return null;
    }
}
