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

    public void menuRecomendacoes(Scanner scanner) {
        int opcRecomendacao;
        do {
            System.out.println("\nMenu Recomendacoes:");
            System.out.println("1. Adicionar Recomendacao");
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
                    System.out.print("Descricao da recomendacao: ");
                    String descricao = scanner.nextLine();
                    Recomendacao recomendacao = new Recomendacao(null, descricao, descricao, true);
                    adicionarRecomendacao(recomendacao);
                    System.out.println("Recomendacao adicionada com sucesso.");
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
        System.out.println("Recomendacoes para o investidor " + investidor.getNome() + ":");
        Node<Ativo> atual = listaAtivos.getHead();

        while (atual != null) {
            Ativo ativo = atual.getData();

            if (ativo.getRisco().equalsIgnoreCase(investidor.getPerfilRisco())
                    && ativo.getRentabilidadeMedia() > 1.0
                    && ativo.getVariacaoPercentual() < 10.0) {
                System.out.printf("Ativo: %s | Rentabilidade: %.2f%% | Risco: %s%n",
                        ativo.getNome(), ativo.getRentabilidadeMedia(), ativo.getRisco());
            }

            atual = atual.getNext();
        }
    }
}
