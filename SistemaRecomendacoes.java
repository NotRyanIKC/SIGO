import java.util.Scanner;

public class SistemaRecomendacoes {
    private DLL<Recomendacao> listaRecomendacoes;

    public SistemaRecomendacoes() {
        this.listaRecomendacoes = new DLL<>();
    }

    public void adicionarRecomendacao(Recomendacao recomendacao) {
        listaRecomendacoes.add(recomendacao);
    }

    public DLL<Recomendacao> getListaRecomendacoes() {
        return listaRecomendacoes;
    }

    public void menuRecomendacoes(Scanner scanner, DLL<Ativo> listaAtivos, DLL<Investidor> listaInvestidores) {
        int opcRecomendacao;
        do {
            System.out.println("\nMenu Recomendacoes:");
            System.out.println("1. Gerar Recomendacoes para um Investidor");
            System.out.println("2. Listar Recomendacoes");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opcao: ");
            
            while (!scanner.hasNextInt()) {
                System.out.println("Digite um numero valido.");
                scanner.next();
            }
            opcRecomendacao = scanner.nextInt();
            scanner.nextLine();

            switch (opcRecomendacao) {
                case 1:
                    if (listaInvestidores.isEmpty()) {
                        System.out.println("Nenhum investidor cadastrado.");
                        break;
                    }
                    System.out.println("\nEscolha um investidor:");
                    Node<Investidor> nodeInv = listaInvestidores.getHead();
                    int idx = 1;
                    while (nodeInv != null) {
                        Investidor inv = nodeInv.getData();
                        System.out.printf("%d) %s%n", idx++, inv.getNome());
                        nodeInv = nodeInv.getNext();
                    }
                    System.out.print("Numero do investidor: ");
                    int sel = scanner.nextInt() - 1;
                    scanner.nextLine();
                    if (sel < 0 || sel >= listaInvestidores.size()) {
                        System.out.println("Investidor invalido.");
                        break;
                    }
                    Investidor investidor = listaInvestidores.get(sel);
                    gerarRecomendacoes(investidor, listaAtivos);
                    break;

                case 2:
                    if (listaRecomendacoes.isEmpty()) {
                        System.out.println("Nenhuma recomendacao cadastrada.");
                    } else {
                        Node<Recomendacao> atual = listaRecomendacoes.getHead();
                        int i = 1;
                        while (atual != null) {
                            System.out.printf("%d) %s%n", i++, atual.getData().getDescricao());
                            atual = atual.getNext();
                        }
                    }
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Opcao invalida. Tente novamente.");
            }
        } while (opcRecomendacao != 0);
    }

    public void gerarRecomendacoes(Investidor investidor, DLL<Ativo> listaAtivos) {
        System.out.println("\nRecomendacoes para o investidor " + investidor.getNome() + ":");
        Node<Ativo> atual = listaAtivos.getHead();
        DLL<String> tiposSugeridos = new DLL<>();

        while (atual != null) {
            Ativo ativo = atual.getData();

            // 1. Compatibilidade de risco
            if (!ativo.getRisco().equalsIgnoreCase(investidor.getPerfilRisco())
                    && !investidor.getPerfilRisco().equalsIgnoreCase("arrojado")) {
                atual = atual.getNext();
                continue;
            }

            // 2. Rentabilidade mínima (ex: 5%)
            if (ativo.getRentabilidadeMedia() < 5.0) {
                atual = atual.getNext();
                continue;
            }

            // 3. Volatilidade controlada
            double maxVariacao = 0;
            switch (investidor.getPerfilRisco().toLowerCase()) {
                case "conservador": maxVariacao = 10; break;
                case "moderado": maxVariacao = 30; break;
                case "arrojado": maxVariacao = Double.MAX_VALUE; break;
            }
            if (Math.abs(ativo.getVariacaoPercentual()) > maxVariacao) {
                atual = atual.getNext();
                continue;
            }

            // 4. Diversificação: evitar repetir o mesmo tipo consecutivo
            Node<String> tipoNode = tiposSugeridos.getHead();
            boolean tipoRepetido = false;
            while (tipoNode != null) {
                if (tipoNode.getData().equalsIgnoreCase(ativo.getTipo())) {
                    tipoRepetido = true;
                    break;
                }
                tipoNode = tipoNode.getNext();
            }
            if (tipoRepetido) {
                atual = atual.getNext();
                continue;
            }

            // Adiciona à lista de recomendações temporária
            tiposSugeridos.add(ativo.getTipo());
            System.out.printf("Ativo: %s | Tipo: %s | Rentabilidade: %.2f%% | Risco: %s | Variacao: %.2f%%%n",
                    ativo.getNome(), ativo.getTipo(), ativo.getRentabilidadeMedia(),
                    ativo.getRisco(), ativo.getVariacaoPercentual());

            atual = atual.getNext();
        }
    }
}
