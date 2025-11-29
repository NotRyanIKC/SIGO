import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        SistemaAtivos sistemaAtivos = new SistemaAtivos();
        SistemaInvestidores sistemaInvestidores = new SistemaInvestidores();
        SistemaRecomendacoes sistemaRecomendacoes = new SistemaRecomendacoes();
        SistemaTransacoes sistemaTransacoes = new SistemaTransacoes();
        SistemaRelatorios sistemaRelatorios = new SistemaRelatorios();

        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("Menu Principal:");
            System.out.println("1. Gerenciar Ativos");
            System.out.println("2. Gerenciar Investidores");
            System.out.println("3. Gerenciar Recomendacoes");
            System.out.println("4. Gerenciar Transacoes");
            System.out.println("5. Gerar Relatorios");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opcao: ");

            while (!scanner.hasNextInt()) {
                System.out.println("Digite um numero valido.");
                scanner.next();
            }
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    sistemaAtivos.menuAtivos(scanner);
                    break;
                case 2:
                    sistemaInvestidores.menuInvestidores(scanner, sistemaAtivos);
                    break;
                case 3:
                    sistemaRecomendacoes.menuRecomendacoes(scanner, sistemaAtivos.getListaAtivos(), sistemaInvestidores.getListaInvestidores());
                    break;
                case 4:
                    sistemaTransacoes.menuTransacoes(scanner);
                    break;
                case 5:
                    sistemaRelatorios.menuRelatorios(scanner, sistemaAtivos, sistemaInvestidores);
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opcao invalida. Tente novamente.");
            }

        } while (opcao != 0);

        scanner.close();
    }
}
