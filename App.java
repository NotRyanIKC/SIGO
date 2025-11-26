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
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    // Chamar funcoes do SistemaAtivos
                    break;
                case 2:
                    // Submenu para gerenciar investidores (incluir cadastro)
                    int opcInvestidor;
                    do {
                        System.out.println("\nMenu Investidores:");
                        System.out.println("1. Cadastrar Investidor");
                        System.out.println("2. Listar Investidores");
                        System.out.println("0. Voltar");
                        System.out.print("Escolha uma opcao: ");
                        opcInvestidor = scanner.nextInt();
                        scanner.nextLine(); // consumir newline

                        switch (opcInvestidor) {
                            case 1:
                                System.out.print("Nome: ");
                                String nome = scanner.nextLine();
                                System.out.print("Idade: ");
                                String idade = scanner.nextLine();
                                System.out.println("Perfil de risco:");
                                System.out.println("1. Conservador");
                                System.out.println("2. Moderado");
                                System.out.println("3. Arrojado");
                                System.out.print("Escolha uma opcao (1-3): ");
                                int perfilOpt = scanner.nextInt();
                                scanner.nextLine();
                                String perfil = null;
                                switch (perfilOpt) {
                                    case 1:
                                        perfil = "conservador";
                                        break;
                                    case 2:
                                        perfil = "moderado";
                                        break;
                                    case 3:
                                        perfil = "arrojado";
                                        break;
                                    default:
                                        System.out.println("Perfil invalido. Cadastro cancelado.");
                                        break;
                                }
                                if (perfil == null) {
                                    // perfil invalido
                                    break;
                                }
                                System.out.print("Capital disponivel: ");
                                double capital;
                                try {
                                    capital = Double.parseDouble(scanner.nextLine());
                                } catch (NumberFormatException e) {
                                    System.out.println("Valor de capital invalido. Cadastro cancelado.");
                                    break;
                                }

                                Investidor novo = new Investidor(nome, idade, perfil, capital);
                                sistemaInvestidores.cadastrarInvestidor(novo);
                                System.out.println("Investidor cadastrado com sucesso!\n");
                                break;

                            case 2:
                                DLL<Investidor> lista = sistemaInvestidores.getListaInvestidores();
                                if (lista.isEmpty()) {
                                    System.out.println("Nenhum investidor cadastrado.");
                                } else {
                                    Node<Investidor> atual = lista.getHead();
                                    int i = 1;
                                    while (atual != null) {
                                        Investidor inv = atual.getData();
                                        System.out.printf("%d) %s - Idade: %s - Perfil: %s - Capital: %.2f\n",
                                                i++, inv.getNome(), inv.getIdade(), inv.getPerfilRisco(), inv.getCapitalDisponivel());
                                        atual = atual.getNext();
                                    }
                                }
                                break;

                            case 0:
                                // Voltar ao menu principal
                                break;

                            default:
                                System.out.println("Opcao invalida. Tente novamente.");
                        }

                    } while (opcInvestidor != 0);
                    break;
                case 3:
                    // Chamar funcoes do SistemaRecomendacoes
                    break;
                case 4:
                    // Chamar funcoes do SistemaTransacoes
                    break;
                case 5:
                    // Chamar funcoes do SistemaRelatorios
                    break;
                
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opcao invalida. Tente novamente.");
            }

        } while (opcao != 0);
    }
    
}
