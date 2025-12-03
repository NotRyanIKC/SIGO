import java.util.Scanner;

public class SistemaInvestidores {
    private DLL<Investidor> listaInvestidores;

    public SistemaInvestidores() {
        this.listaInvestidores = new DLL<>();
    }

    public void cadastrarInvestidor(Investidor investidor) {
        listaInvestidores.add(investidor);
    }

    // -----------------------------------------
    // FAZER INVESTIMENTO COM VALIDAÇÕES
    // -----------------------------------------
    public boolean fazerInvestimento(Investidor inv, Ativo ativo, double valor) {

        if (valor <= 0) {
            System.out.println("O valor do investimento deve ser maior que zero.");
            return false;
        }

        if (!ativo.getRisco().equalsIgnoreCase(inv.getPerfilRisco())
                && !inv.getPerfilRisco().equalsIgnoreCase("arrojado")) {
            System.out.printf("ATENÇÃO: O risco do ativo (%s) não se encaixa no seu perfil (%s). Deseja continuar? (S/N): ",
                    ativo.getRisco(), inv.getPerfilRisco());
            Scanner scanner = new Scanner(System.in);
            String resposta = scanner.nextLine().trim().toUpperCase();
            if (!resposta.equals("S")) {
                System.out.println("Investimento cancelado pelo usuário.");
                return false;
            }
        }

        if (ativo.getRisco().equalsIgnoreCase("alto")
                && valor > inv.getCapitalDisponivel() * 0.8) {
            System.out.println("Nao é permitido investir mais de 80% do capital disponivel em ativos de risco alto.");
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

    // -----------------------------------------
    // RESGATAR INVESTIMENTO COM VALIDAÇÕES
    // -----------------------------------------
    public boolean resgatarInvestimento(Investidor inv, Ativo ativo, double valor) {

        if (valor <= 0) {
            System.out.println("O valor do resgate deve ser maior que zero.");
            return false;
        }

        DLL<Investimento> historico = inv.getHistoricoInvestimentos();
        Node<Investimento> atual = historico.getHead();

        while (atual != null) {
            Investimento investimento = atual.getData();
            if (investimento.getAtivo().equals(ativo)) {

                if (valor > investimento.getValorAplicado()) {
                    System.out.println("Valor de resgate maior que o valor aplicado.");
                    return false;
                }

                double proporcao = valor / investimento.getValorAplicado();
                double lucroPrejuizo = (ativo.getValorAtual() - investimento.getValorInicial()) * proporcao;

                investimento.setValorAplicado(investimento.getValorAplicado() - valor);
                inv.setCapitalDisponivel(inv.getCapitalDisponivel() + valor + lucroPrejuizo);

                System.out.printf("Resgate realizado com sucesso! Lucro/Prejuízo: R$ %.2f%n", lucroPrejuizo);
                return true;
            }
            atual = atual.getNext();
        }

        System.out.println("Investimento não encontrado.");
        return false;
    }

    public DLL<Investidor> getListaInvestidores() {
        return listaInvestidores;
    }

    // -----------------------------------------
    // HISTÓRICO DE INVESTIMENTOS
    // -----------------------------------------
    private void verHistorico(Investidor inv) {
        DLL<Investimento> historico = inv.getHistoricoInvestimentos();

        if (historico.isEmpty()) {
            System.out.println("Nenhum investimento realizado.");
            return;
        }

        System.out.println("\nHistórico de Investimentos de " + inv.getNome() + ":");
        Node<Investimento> atual = historico.getHead();
        int i = 1;

        while (atual != null) {
            Investimento invst = atual.getData();
            double lucroPrejuizo = invst.getAtivo().getValorAtual() - invst.getValorAplicado();
            System.out.printf("%d) Ativo: %s | Valor Investido: %.2f | Valor Atual: %.2f | Lucro/Prejuízo: %.2f%n",
                    i++, invst.getAtivo().getNome(), invst.getValorAplicado(),
                    invst.getAtivo().getValorAtual(), lucroPrejuizo);
            atual = atual.getNext();
        }
    }

    // -----------------------------------------
    // MENU PRINCIPAL DO SISTEMA DE INVESTIDORES
    // -----------------------------------------
    public void menuInvestidores(Scanner scanner, SistemaAtivos sistemaAtivos) {
        int opcInvestidor;
        do {
            System.out.println("\nMenu Investidores:");
            System.out.println("1. Cadastrar Investidor");
            System.out.println("2. Listar Investidores");
            System.out.println("3. Fazer Investimento");
            System.out.println("4. Ver histórico de investimentos");
            System.out.println("5. Venda de Ativo (Resgate)");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opcao: ");

            while (!scanner.hasNextInt()) {
                System.out.println("Digite um numero valido.");
                scanner.next();
            }
            opcInvestidor = scanner.nextInt();
            scanner.nextLine();

            switch (opcInvestidor) {

                // -----------------------------------------
                // 1 - CADASTRAR INVESTIDOR
                // -----------------------------------------
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

                    int perfilOpt;
                    while (!scanner.hasNextInt()) {
                        System.out.println("Digite um número válido.");
                        scanner.next();
                    }
                    perfilOpt = scanner.nextInt();
                    scanner.nextLine();

                    String perfil = switch (perfilOpt) {
                        case 1 -> "conservador";
                        case 2 -> "moderado";
                        case 3 -> "arrojado";
                        default -> null;
                    };

                    if (perfil == null) {
                        System.out.println("Perfil inválido. Cadastro cancelado.");
                        break;
                    }

                    // CAPITAL COM VALIDAÇÃO
                    double capital;
                    while (true) {
                        System.out.print("Capital disponível: ");
                        try {
                            capital = Double.parseDouble(scanner.nextLine());
                            if (capital <= 0) {
                                System.out.println("O capital deve ser maior que zero.");
                                continue;
                            }
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Digite um valor numérico válido.");
                        }
                    }

                    Investidor novo = new Investidor(nome, idade, perfil, capital);
                    cadastrarInvestidor(novo);
                    System.out.println("Investidor cadastrado com sucesso.");
                    break;

                // -----------------------------------------
                // 2 - LISTAR
                // -----------------------------------------
                case 2:
                    if (listaInvestidores.isEmpty()) {
                        System.out.println("Nenhum investidor cadastrado.");
                        break;
                    }

                    Node<Investidor> atual = listaInvestidores.getHead();
                    int i = 1;

                    while (atual != null) {
                        Investidor inv = atual.getData();
                        System.out.printf("%d) %s - Idade: %s - Perfil: %s - Capital: %.2f%n",
                                i++, inv.getNome(), inv.getIdade(), inv.getPerfilRisco(), inv.getCapitalDisponivel());
                        atual = atual.getNext();
                    }
                    break;

                // -----------------------------------------
                // 3 - FAZER INVESTIMENTO
                // -----------------------------------------
                case 3:
                    if (listaInvestidores.isEmpty()) {
                        System.out.println("Nenhum investidor cadastrado.");
                        break;
                    }

                    System.out.println("\nLista de Investidores:");
                    Node<Investidor> atualInv = listaInvestidores.getHead();
                    int idx = 1;

                    while (atualInv != null) {
                        Investidor inv = atualInv.getData();
                        System.out.printf("%d) %s - Capital Disponível: %.2f%n",
                                idx++, inv.getNome(), inv.getCapitalDisponivel());
                        atualInv = atualInv.getNext();
                    }

                    System.out.print("Escolha o número do investidor: ");
                    int investidorIndex = scanner.nextInt() - 1;
                    scanner.nextLine();

                    if (investidorIndex < 0 || investidorIndex >= listaInvestidores.size()) {
                        System.out.println("Investidor inválido.");
                        break;
                    }

                    Investidor investidorSelecionado = listaInvestidores.get(investidorIndex);

                    DLL<Ativo> listaAtivos = sistemaAtivos.getListaAtivos();

                    if (listaAtivos.isEmpty()) {
                        System.out.println("Nenhum ativo cadastrado.");
                        break;
                    }

                    System.out.println("\nLista de Ativos:");
                    Node<Ativo> atualAtivo = listaAtivos.getHead();
                    int j = 1;

                    while (atualAtivo != null) {
                        Ativo a = atualAtivo.getData();
                        System.out.printf("%d) %s - %s - Risco: %s - Valor Atual: %.2f%n",
                                j++, a.getCodigo(), a.getNome(), a.getRisco(), a.getValorAtual());
                        atualAtivo = atualAtivo.getNext();
                    }

                    System.out.print("Escolha o número do ativo: ");
                    int ativoIndex = scanner.nextInt() - 1;
                    scanner.nextLine();

                    if (ativoIndex < 0 || ativoIndex >= listaAtivos.size()) {
                        System.out.println("Ativo inválido.");
                        break;
                    }

                    Ativo ativoSelecionado = listaAtivos.get(ativoIndex);

                    double valorInvestir;
                    while (true) {
                        System.out.print("Valor a investir: ");
                        try {
                            valorInvestir = Double.parseDouble(scanner.nextLine());
                            if (valorInvestir <= 0) {
                                System.out.println("O valor deve ser maior que zero.");
                                continue;
                            }
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Digite um valor numérico válido.");
                        }
                    }

                    fazerInvestimento(investidorSelecionado, ativoSelecionado, valorInvestir);
                    break;


                case 4:
                    if (listaInvestidores.isEmpty()) {
                        System.out.println("Nenhum investidor cadastrado.");
                        break;
                    }

                    System.out.println("\nEscolha o investidor para ver o histórico:");
                    Node<Investidor> atualHist = listaInvestidores.getHead();
                    int idxHist = 1;

                    while (atualHist != null) {
                        Investidor invHist = atualHist.getData();
                        System.out.printf("%d) %s%n", idxHist++, invHist.getNome());
                        atualHist = atualHist.getNext();
                    }

                    System.out.print("Número do investidor: ");
                    int investidorHistIndex = scanner.nextInt() - 1;
                    scanner.nextLine();

                    if (investidorHistIndex < 0 || investidorHistIndex >= listaInvestidores.size()) {
                        System.out.println("Investidor inválido.");
                        break;
                    }

                    Investidor investidorSelecionadoHist = listaInvestidores.get(investidorHistIndex);
                    verHistorico(investidorSelecionadoHist);
                    break;

                // -----------------------------------------
                // 5 - VENDA DE ATIVO (RESGATE)
                // -----------------------------------------
                case 5:
                    // Venda de Ativo (Resgate) — seleciona investidor e ativo por índice e chama resgatarInvestimento
                    if (listaInvestidores.isEmpty()) {
                        System.out.println("Nenhum investidor cadastrado.");
                        break;
                    }

                    System.out.println("\nLista de Investidores:");
                    Node<Investidor> atualVendaInv = listaInvestidores.getHead();
                    int idxVenda = 1;
                    while (atualVendaInv != null) {
                        Investidor inv = atualVendaInv.getData();
                        System.out.printf("%d) %s - Capital Disponível: %.2f%n", idxVenda++, inv.getNome(), inv.getCapitalDisponivel());
                        atualVendaInv = atualVendaInv.getNext();
                    }

                    System.out.print("Escolha o número do investidor: ");
                    int investidorVendaIndex = scanner.nextInt() - 1;
                    scanner.nextLine();

                    if (investidorVendaIndex < 0 || investidorVendaIndex >= listaInvestidores.size()) {
                        System.out.println("Investidor inválido.");
                        break;
                    }

                    Investidor investidorSelecionadoVenda = listaInvestidores.get(investidorVendaIndex);

                    DLL<Ativo> listaAtivosVenda = sistemaAtivos.getListaAtivos();
                    if (listaAtivosVenda.isEmpty()) {
                        System.out.println("Nenhum ativo cadastrado.");
                        break;
                    }

                    System.out.println("\nLista de Ativos:");
                    Node<Ativo> atualAtivoVenda = listaAtivosVenda.getHead();
                    int jVenda = 1;
                    while (atualAtivoVenda != null) {
                        Ativo a = atualAtivoVenda.getData();
                        System.out.printf("%d) %s - %s - Risco: %s - Valor Atual: %.2f%n",
                                jVenda++, a.getCodigo(), a.getNome(), a.getRisco(), a.getValorAtual());
                        atualAtivoVenda = atualAtivoVenda.getNext();
                    }

                    System.out.print("Escolha o número do ativo: ");
                    int ativoVendaIndex = scanner.nextInt() - 1;
                    scanner.nextLine();

                    if (ativoVendaIndex < 0 || ativoVendaIndex >= listaAtivosVenda.size()) {
                        System.out.println("Ativo inválido.");
                        break;
                    }

                    Ativo ativoSelecionadoVenda = listaAtivosVenda.get(ativoVendaIndex);

                    double valorResgatar;
                    while (true) {
                        System.out.print("Valor a resgatar: ");
                        try {
                            valorResgatar = Double.parseDouble(scanner.nextLine());
                            if (valorResgatar <= 0) {
                                System.out.println("O valor deve ser maior que zero.");
                                continue;
                            }
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Digite um valor numérico válido.");
                        }
                    }

                    // Chama o método já existente
                    resgatarInvestimento(investidorSelecionadoVenda, ativoSelecionadoVenda, valorResgatar);
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Opção inválida.");
            }

        } while (opcInvestidor != 0);
    }
}
