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
}
