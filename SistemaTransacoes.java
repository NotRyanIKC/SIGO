import java.util.Scanner;

public class SistemaTransacoes {
    private DLL<Transacao> listaTransacoes;

    public SistemaTransacoes() {
        this.listaTransacoes = new DLL<>();
    }

    public void registrar(Transacao t) {
        listaTransacoes.add(t);
    }

    public DLL<Transacao> getListaTransacoes() {
        return listaTransacoes;
    }

    public void menuTransacoes(Scanner scanner) {
        int opcTransacao;
        do {
            System.out.println("\nMenu Transacoes:");
            System.out.println("1. Registrar Transacao");
            System.out.println("2. Listar Transacoes");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opcao: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Digite um numero valido.");
                scanner.next();
            }
            opcTransacao = scanner.nextInt();
            scanner.nextLine();

            switch (opcTransacao) {
                case 1:
                    System.out.print("Descricao da transacao: ");
                    String descricao = scanner.nextLine();
                    System.out.print("Valor da transacao: ");
                    double valor = scanner.nextDouble();
                    scanner.nextLine();
                    Transacao transacao = new Transacao(descricao, valor);
                    registrar(transacao);
                    System.out.println("Transacao registrada com sucesso.");
                    break;

                case 2:
                    if (listaTransacoes.isEmpty()) {
                        System.out.println("Nenhuma transacao registrada.");
                    } else {
                        Node<Transacao> atual = listaTransacoes.getHead();
                        int i = 1;
                        while (atual != null) {
                            System.out.printf("%d) %s - Valor: %.2f%n", i++, atual.getData().getDescricao(), atual.getData().getValor());
                            atual = atual.getNext();
                        }
                    }
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Opcao invalida. Tente novamente.");
            }
        } while (opcTransacao != 0);
    }
}
