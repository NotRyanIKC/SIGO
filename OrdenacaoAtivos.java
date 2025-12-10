public class OrdenacaoAtivos {
    public void insertionSortRentabilidade(DLL<Ativo> lista) {
        if (lista == null || lista.size() <= 1) {
            return;
        }

        Node<Ativo> atual = lista.getHead().getNext();
        while (atual != null) {
            Ativo chave = atual.getData();
            Node<Ativo> anterior = atual.getPrev();

            while (anterior != null && anterior.getData().getRentabilidadeMedia() > chave.getRentabilidadeMedia()) {
                anterior.getNext().setData(anterior.getData());
                anterior = anterior.getPrev();
            }

            if (anterior == null) {
                lista.getHead().setData(chave);
            } else {
                anterior.getNext().setData(chave);
            }

            atual = atual.getNext();
        }
    }

    public DLL<Ativo> mergeSortVariacao(DLL<Ativo> lista) {
        if (lista.size() <= 1) {
            return lista;
        }

        DLL<Ativo> esquerda = new DLL<>();
        DLL<Ativo> direita = new DLL<>();
        Node<Ativo> atual = lista.getHead();
        int meio = lista.size() / 2;

        for (int i = 0; i < meio; i++) {
            esquerda.add(atual.getData());
            atual = atual.getNext();
        }
        while (atual != null) {
            direita.add(atual.getData());
            atual = atual.getNext();
        }

        esquerda = mergeSortVariacao(esquerda);
        direita = mergeSortVariacao(direita);

        return merge(esquerda, direita);
    }

    private DLL<Ativo> merge(DLL<Ativo> esquerda, DLL<Ativo> direita) {
        DLL<Ativo> resultado = new DLL<>();
        Node<Ativo> e = esquerda.getHead();
        Node<Ativo> d = direita.getHead();

        while (e != null && d != null) {
            if (e.getData().getVariacaoPercentual() <= d.getData().getVariacaoPercentual()) {
                resultado.add(e.getData());
                e = e.getNext();
            } else {
                resultado.add(d.getData());
                d = d.getNext();
            }
        }

        while (e != null) {
            resultado.add(e.getData());
            e = e.getNext();
        }
        while (d != null) {
            resultado.add(d.getData());
            d = d.getNext();
        }

        return resultado;
    }

    public void quickSortValorAtual(Node<Ativo> inicio, Node<Ativo> fim) {
        if (inicio == null || fim == null || inicio == fim) {
            return;
        }

        Node<Ativo> pivo = partition(inicio, fim);
        if (pivo != null && pivo.getPrev() != null) {
            quickSortValorAtual(inicio, pivo.getPrev());
        }
        if (pivo != null && pivo.getNext() != null) {
            quickSortValorAtual(pivo.getNext(), fim);
        }
    }

    private Node<Ativo> partition(Node<Ativo> inicio, Node<Ativo> fim) {
        Ativo pivo = fim.getData();
        Node<Ativo> i = inicio.getPrev();

        for (Node<Ativo> j = inicio; j != fim; j = j.getNext()) {
            if (j.getData().getValorAtual() <= pivo.getValorAtual()) {
                i = (i == null) ? inicio : i.getNext();
                Ativo temp = i.getData();
                i.setData(j.getData());
                j.setData(temp);
            }
        }

        i = (i == null) ? inicio : i.getNext();
        Ativo temp = i.getData();
        i.setData(fim.getData());
        fim.setData(temp);

        return i;
    }

    public void ordenarPorRentabilidadeComMetricas(DLL<Ativo> lista) {
        if (lista == null || lista.size() <= 1) {
            System.out.println("Lista vazia ou com um único elemento. Ordenação não necessária.");
            return;
        }

        long inicio = System.nanoTime();
        int comparacoes = 0, movimentacoes = 0;

        Node<Ativo> atual = lista.getHead().getNext();
        while (atual != null) {
            Ativo chave = atual.getData();
            Node<Ativo> anterior = atual.getPrev();

            while (anterior != null && anterior.getData().getRentabilidadeMedia() > chave.getRentabilidadeMedia()) {
                comparacoes++;
                anterior.getNext().setData(anterior.getData());
                movimentacoes++;
                anterior = anterior.getPrev();
            }

            if (anterior == null) {
                lista.getHead().setData(chave);
            } else {
                anterior.getNext().setData(chave);
            }
            movimentacoes++;
            atual = atual.getNext();
        }

        long fim = System.nanoTime();
        System.out.printf("Insertion Sort - Rentabilidade: Tempo: %.2f ms, Comparações: %d, Movimentações: %d%n",
                (fim - inicio) / 1_000_000.0, comparacoes, movimentacoes);
    }

    public DLL<Ativo> ordenarPorVariacaoComMetricas(DLL<Ativo> lista) {
        Contador contador = new Contador();
        long inicio = System.nanoTime();
        DLL<Ativo> resultado = mergeSortVariacao(lista, contador);
        long fim = System.nanoTime();
        System.out.printf("Merge Sort - Variação: Tempo: %.2f ms, Comparações: %d, Movimentações: %d%n",
                (fim - inicio) / 1_000_000.0, contador.comparacoes, contador.movimentacoes);
        return resultado;
    }

    private DLL<Ativo> mergeSortVariacao(DLL<Ativo> lista, Contador contador) {
        if (lista.size() <= 1) {
            return lista;
        }

        DLL<Ativo> esquerda = new DLL<>();
        DLL<Ativo> direita = new DLL<>();
        Node<Ativo> atual = lista.getHead();
        int meio = lista.size() / 2;

        for (int i = 0; i < meio; i++) {
            esquerda.add(atual.getData());
            atual = atual.getNext();
        }
        while (atual != null) {
            direita.add(atual.getData());
            atual = atual.getNext();
        }

        esquerda = mergeSortVariacao(esquerda, contador);
        direita = mergeSortVariacao(direita, contador);

        return merge(esquerda, direita, contador);
    }

    private DLL<Ativo> merge(DLL<Ativo> esquerda, DLL<Ativo> direita, Contador contador) {
        DLL<Ativo> resultado = new DLL<>();
        Node<Ativo> e = esquerda.getHead();
        Node<Ativo> d = direita.getHead();

        while (e != null && d != null) {
            contador.comparacoes++;
            if (e.getData().getVariacaoPercentual() <= d.getData().getVariacaoPercentual()) {
                resultado.add(e.getData());
                e = e.getNext();
            } else {
                resultado.add(d.getData());
                d = d.getNext();
            }
            contador.movimentacoes++;
        }

        while (e != null) {
            resultado.add(e.getData());
            e = e.getNext();
            contador.movimentacoes++;
        }
        while (d != null) {
            resultado.add(d.getData());
            d = d.getNext();
            contador.movimentacoes++;
        }

        return resultado;
    }

    public void ordenarPorValorAtualComMetricas(DLL<Ativo> lista) {
        Contador contador = new Contador();
        long inicio = System.nanoTime();
        quickSortValorAtual(lista.getHead(), lista.getTail(), contador);
        long fim = System.nanoTime();
        System.out.printf("Quick Sort - Valor Atual: Tempo: %.2f ms, Comparações: %d, Movimentações: %d%n",
                (fim - inicio) / 1_000_000.0, contador.comparacoes, contador.movimentacoes);
    }

    private void quickSortValorAtual(Node<Ativo> inicio, Node<Ativo> fim, Contador contador) {
        if (inicio == null || fim == null || inicio == fim) {
            return;
        }

        Node<Ativo> pivo = partition(inicio, fim, contador);
        if (pivo != null && pivo.getPrev() != null) {
            quickSortValorAtual(inicio, pivo.getPrev(), contador);
        }
        if (pivo != null && pivo.getNext() != null) {
            quickSortValorAtual(pivo.getNext(), fim, contador);
        }
    }

    private Node<Ativo> partition(Node<Ativo> inicio, Node<Ativo> fim, Contador contador) {
        Ativo pivo = fim.getData();
        Node<Ativo> i = inicio.getPrev();

        for (Node<Ativo> j = inicio; j != fim; j = j.getNext()) {
            contador.comparacoes++;
            if (j.getData().getValorAtual() <= pivo.getValorAtual()) {
                i = (i == null) ? inicio : i.getNext();
                Ativo temp = i.getData();
                i.setData(j.getData());
                j.setData(temp);
                contador.movimentacoes++;
            }
        }

        i = (i == null) ? inicio : i.getNext();
        Ativo temp = i.getData();
        i.setData(fim.getData());
        fim.setData(temp);
        contador.movimentacoes++;

        return i;
    }

    public void ordenarPorCodigo(DLL<Ativo> lista) {
        if (lista == null || lista.size() <= 1) {
            return;
        }

        Node<Ativo> atual = lista.getHead().getNext();
        while (atual != null) {
            Ativo chave = atual.getData();
            Node<Ativo> anterior = atual.getPrev();

            while (anterior != null && anterior.getData().getCodigo().compareTo(chave.getCodigo()) > 0) {
                anterior.getNext().setData(anterior.getData());
                anterior = anterior.getPrev();
            }

            if (anterior == null) {
                lista.getHead().setData(chave);
            } else {
                anterior.getNext().setData(chave);
            }

            atual = atual.getNext();
        }
    }
}

class Contador {
    int comparacoes = 0;
    int movimentacoes = 0;
}
