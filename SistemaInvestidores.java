import java.util.Scanner;

public class SistemaInvestidores {
    private DLL<Investidor> listaInvestidores;

    public SistemaInvestidores() {
        this.listaInvestidores = new DLL<>();
    }

    public void cadastrarInvestidor(Investidor investidor) {
        listaInvestidores.add(investidor);
    }

    public void investir(Investidor inv, Ativo ativo, double valor) {
    }

    public void resgatar(Investidor inv, Ativo ativo, double valor) {
    }

    public boolean fazerInvestimento(Investidor inv, Ativo ativo, double valor) {
        if (!ativo.getRisco().equalsIgnoreCase(inv.getPerfilRisco()) && !inv.getPerfilRisco().equalsIgnoreCase("arrojado")) {
            System.out.println("Investimento incompativel com o perfil de risco do investidor.");
            return false;
        }

        if (ativo.getRisco().equalsIgnoreCase("alto") && valor > inv.getCapitalDisponivel() * 0.8) {
            System.out.println("Nao e permitido investir mais de 80% do capital disponivel em ativos de risco alto.");
            return false;
        }

        if (valor > inv.getCapitalDisponivel()) {
            System.out.println("Capital insuficiente para realizar o investimento.");
            return false;
        }

        double valorInicial = ativo.getValorAtual();
        Investimento investimento = new Investimento(ativo, valor, valorInicial, 0);
        inv.getHistoricoInvestimentos().add(investimento);
        inv.setCapitalDisponivel(inv.getCapitalDisponivel() - valor);

        System.out.println("Investimento realizado com sucesso!");
        return true;
    }

    public boolean resgatarInvestimento(Investidor inv, Ativo ativo, double valor) {
        DLL<Investimento> historico = inv.getHistoricoInvestimentos();
        Node<Investimento> atual = historico.getHead();

        while (atual != null) {
            Investimento investimento = atual.getData();
            if (investimento.getAtivo().equals(ativo)) {
                if (valor > investimento.getValorAplicado()) {
                    System.out.println("Valor de resgate maior que o valor aplicado.");
                    return false;
                }

                double lucroPrejuizo = (ativo.getValorAtual() - investimento.getValorInicial()) * (valor / investimento.getValorAplicado());
                investimento.setValorAplicado(investimento.getValorAplicado() - valor);
                inv.setCapitalDisponivel(inv.getCapitalDisponivel() + valor + lucroPrejuizo);

                System.out.printf("Resgate realizado com sucesso! Lucro/Prejuizo: R$ %.2f%n", lucroPrejuizo);
                return true;
            }
            atual = atual.getNext();
        }

        System.out.println("Investimento nao encontrado.");
        return false;
    }

    public DLL<Investidor> getListaInvestidores() {
        return listaInvestidores;
    }

    public void menuInvestidores(Scanner scanner, SistemaAtivos sistemaAtivos) {
        int opcInvestidor;
        do {
            System.out.println("\nMenu Investidores:");
            System.out.println("1. Cadastrar Investidor");
            System.out.println("2. Listar Investidores");
            System.out.println("3. Fazer Investimento");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opcao: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Digite um numero valido.");
                scanner.next();
            }
            opcInvestidor = scanner.nextInt();
            scanner.nextLine();

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
                    String perfil = switch (perfilOpt) {
                        case 1 -> "conservador";
                        case 2 -> "moderado";
                        case 3 -> "arrojado";
                        default -> null;
                    };
                    if (perfil == null) {
                        System.out.println("Perfil invalido. Cadastro cancelado.");
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
                    cadastrarInvestidor(novo);
                    System.out.println("Investidor cadastrado com sucesso.");
                    break;

                case 2:
                    if (listaInvestidores.isEmpty()) {
                        System.out.println("Nenhum investidor cadastrado.");
                    } else {
                        Node<Investidor> atual = listaInvestidores.getHead();
                        int i = 1;
                        while (atual != null) {
                            Investidor inv = atual.getData();
                            System.out.printf("%d) %s - Idade: %s - Perfil: %s - Capital: %.2f%n",
                                    i++, inv.getNome(), inv.getIdade(), inv.getPerfilRisco(), inv.getCapitalDisponivel());
                            atual = atual.getNext();
                        }
                    }
                    break;

                case 3:
                    System.out.println("Lista de Investidores:");
                    if (listaInvestidores.isEmpty()) {
                        System.out.println("Nenhum investidor cadastrado.");
                        break;
                    }
                    Node<Investidor> atualInvestidor = listaInvestidores.getHead();
                    int i = 1;
                    while (atualInvestidor != null) {
                        Investidor inv = atualInvestidor.getData();
                        System.out.printf("%d) %s - Capital Disponivel: %.2f%n", i++, inv.getNome(), inv.getCapitalDisponivel());
                        atualInvestidor = atualInvestidor.getNext();
                    }
                    System.out.print("Escolha o numero do investidor: ");
                    int investidorIndex = scanner.nextInt() - 1;
                    scanner.nextLine();
                    if (investidorIndex < 0 || investidorIndex >= listaInvestidores.size()) {
                        System.out.println("Investidor invalido.");
                        break;
                    }
                    Investidor investidorSelecionado = listaInvestidores.get(investidorIndex);

                    DLL<Ativo> listaAtivos = sistemaAtivos.getListaAtivos();
                    if (listaAtivos.isEmpty()) {
                        System.out.println("Nenhum ativo cadastrado. Cadastre ativos antes de investir.");
                        break;
                    }
                    System.out.println("Lista de Ativos:");
                    Node<Ativo> atualAtivo = listaAtivos.getHead();
                    int j = 1;
                    while (atualAtivo != null) {
                        Ativo a = atualAtivo.getData();
                        System.out.printf("%d) %s - %s - Risco: %s - Valor Atual: %.2f%n",
                                j++, a.getCodigo(), a.getNome(), a.getRisco(), a.getValorAtual());
                        atualAtivo = atualAtivo.getNext();
                    }
                    System.out.print("Escolha o numero do ativo: ");
                    int ativoIndex = scanner.nextInt() - 1;
                    scanner.nextLine();
                    if (ativoIndex < 0 || ativoIndex >= listaAtivos.size()) {
                        System.out.println("Ativo invalido.");
                        break;
                    }
                    Ativo ativoSelecionado = listaAtivos.get(ativoIndex);
                    System.out.print("Valor a investir: ");
                    double valorInvestir = scanner.nextDouble();
                    scanner.nextLine();
                    fazerInvestimento(investidorSelecionado, ativoSelecionado, valorInvestir);
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Opcao invalida. Tente novamente.");
            }
        } while (opcInvestidor != 0);
    }
}
