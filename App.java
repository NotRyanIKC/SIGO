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
                    // Submenu para gerenciar ativos
                    int opcAtivo;
                    do {
                        System.out.println("\nMenu Ativos:");
                        System.out.println("1. Cadastrar Ativo");
                        System.out.println("2. Listar Ativos");
                        System.out.println("3. Buscar Ativo por Nome");
                        System.out.println("4. Buscar Ativo por Código");
                        System.out.println("0. Voltar");
                        System.out.print("Escolha uma opcao: ");
                        opcAtivo = scanner.nextInt();
                        scanner.nextLine(); // Consumir newline

                        switch (opcAtivo) {
                            case 1:
                                System.out.print("Código: ");
                                String codigo = scanner.nextLine();
                                System.out.print("Nome: ");
                                String nome = scanner.nextLine();
                                System.out.print("Tipo: ");
                                String tipo = scanner.nextLine();
                                System.out.print("Risco: ");
                                String risco = scanner.nextLine();
                                System.out.print("Rentabilidade Média: ");
                                double rentabilidadeMedia = scanner.nextDouble();
                                System.out.print("Valor Atual: ");
                                double valorAtual = scanner.nextDouble();
                                System.out.print("Variação Percentual: ");
                                double variacaoPercentual = scanner.nextDouble();
                                scanner.nextLine(); // Consumir newline

                                Ativo ativo = new Ativo(codigo, nome, tipo, risco, rentabilidadeMedia, valorAtual, variacaoPercentual, 0);
                                sistemaAtivos.cadastrarAtivo(ativo);
                                System.out.println("Ativo cadastrado com sucesso!");
                                break;

                            case 2:
                                DLL<Ativo> listaAtivos = sistemaAtivos.getListaAtivos();
                                if (listaAtivos.isEmpty()) {
                                    System.out.println("Nenhum ativo cadastrado.");
                                } else {
                                    Node<Ativo> atual = listaAtivos.getHead();
                                    int i = 1;
                                    while (atual != null) {
                                        Ativo atv = atual.getData();
                                        System.out.printf("%d) Código: %s - Nome: %s - Tipo: %s - Risco: %s - Valor Atual: %.2f\n",
                                                i++, atv.getCodigo(), atv.getNome(), atv.getTipo(), atv.getRisco(), atv.getValorAtual());
                                        atual = atual.getNext();
                                    }
                                }
                                break;

                            case 3:
                                System.out.print("Digite o nome do ativo: ");
                                String nomeBusca = scanner.nextLine();
                                Ativo encontradoNome = new BuscaAtivos().buscaLinear(sistemaAtivos.getListaAtivos(), nomeBusca);
                                if (encontradoNome != null) {
                                    System.out.printf("Ativo encontrado: Código: %s - Nome: %s - Tipo: %s - Valor Atual: %.2f\n",
                                            encontradoNome.getCodigo(), encontradoNome.getNome(), encontradoNome.getTipo(), encontradoNome.getValorAtual());
                                } else {
                                    System.out.println("Ativo não encontrado.");
                                }
                                break;

                            case 4:
                                System.out.print("Digite o código do ativo: ");
                                String codigoBusca = scanner.nextLine();
                                Ativo encontradoCodigo = new BuscaAtivos().buscaBinaria(sistemaAtivos.getListaAtivos(), codigoBusca);
                                if (encontradoCodigo != null) {
                                    System.out.printf("Ativo encontrado: Código: %s - Nome: %s - Tipo: %s - Valor Atual: %.2f\n",
                                            encontradoCodigo.getCodigo(), encontradoCodigo.getNome(), encontradoCodigo.getTipo(), encontradoCodigo.getValorAtual());
                                } else {
                                    System.out.println("Ativo não encontrado.");
                                }
                                break;

                            case 0:
                                // Voltar ao menu principal
                                break;

                            default:
                                System.out.println("Opcao invalida. Tente novamente.");
                        }
                    } while (opcAtivo != 0);
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
