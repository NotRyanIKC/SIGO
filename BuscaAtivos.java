public class BuscaAtivos {
    public Ativo buscaLinear(DLL<Ativo> lista, String nome) {
        long inicio = System.nanoTime();
        int comparacoes = 0;

        for (int i = 0; i < lista.size(); i++) {
            comparacoes++;
            Ativo ativo = lista.get(i);
            if (ativo.getNome().equalsIgnoreCase(nome)) {
                long fim = System.nanoTime();
                System.out.printf("Busca linear concluída em %.2f ms com %d comparações.%n",
                        (fim - inicio) / 1_000_000.0, comparacoes);
                return ativo;
            }
        }

        long fim = System.nanoTime();
        System.out.printf("Busca linear concluída em %.2f ms com %d comparações.%n",
                (fim - inicio) / 1_000_000.0, comparacoes);
        return null;
    }

    public Ativo buscaBinaria(DLL<Ativo> lista, String codigo) {
        // Pré-condição: lista deve estar ordenada por código.
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
                System.out.printf("Busca binária concluída em %.2f ms com %d comparações.%n",
                        (fim - inicio) / 1_000_000.0, comparacoes);
                return ativoMeio;
            } else if (comparacao < 0) {
                inicioIdx = meio + 1;
            } else {
                fimIdx = meio - 1;
            }
        }

        long fim = System.nanoTime();
        System.out.printf("Busca binária concluída em %.2f ms com %d comparações.%n",
                (fim - inicio) / 1_000_000.0, comparacoes);
        return null;
    }

    public Ativo buscaLinearPorTipo(DLL<Ativo> lista, String tipo) {
        long inicio = System.nanoTime();
        int comparacoes = 0;

        for (int i = 0; i < lista.size(); i++) {
            comparacoes++;
            Ativo ativo = lista.get(i);
            if (ativo.getTipo().equalsIgnoreCase(tipo)) {
                long fim = System.nanoTime();
                System.out.printf("Busca linear por tipo concluída em %.2f ms com %d comparações.%n",
                        (fim - inicio) / 1_000_000.0, comparacoes);
                return ativo;
            }
        }

        long fim = System.nanoTime();
        System.out.printf("Busca linear por tipo concluída em %.2f ms com %d comparações.%n",
                (fim - inicio) / 1_000_000.0, comparacoes);
        return null;
    }
}
