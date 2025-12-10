import java.util.Scanner;
import java.time.LocalDate;

public class SistemaInvestidores {
    private DLL<Investidor> listaInvestidores;

    public SistemaInvestidores() {
        this.listaInvestidores = new DLL<>();
    }

    public void cadastrarInvestidor(Investidor investidor) {
        listaInvestidores.add(investidor);
    }

    private int nivelRiscoAtivo(String risco) {
        if (risco == null) return 0;
        risco = risco.toLowerCase();
        return switch (risco) {
            case "baixo" -> 1;
            case "medio" -> 2;
            case "alto"  -> 3;
            default      -> 0;
        };
    }

    private int nivelRiscoPerfil(String perfil) {
        if (perfil == null) return 0;
        perfil = perfil.toLowerCase();
        return switch (perfil) {
            case "conservador" -> 1;
            case "moderado"    -> 2;
            case "arrojado"    -> 3;
            default            -> 0;
        };
    }

    private boolean riscoAtivoAcimaDoPerfil(Investidor inv, Ativo ativo) {
        int nAtivo  = nivelRiscoAtivo(ativo.getRisco());
        int nPerfil = nivelRiscoPerfil(inv.getPerfilRisco());
        return nAtivo > nPerfil;
    }

    public boolean fazerInvestimento(Investidor inv, Ativo ativo, double valor, SistemaTransacoes sistemaTransacoes, Scanner scanner) {

        if (valor <= 0) {
            System.out.println("O valor do investimento deve ser maior que zero.");
            return false;
        }

        // compatibilidade de risco usando níveis
        if (riscoAtivoAcimaDoPerfil(inv, ativo)) {
            System.out.printf(
                    "ATENÇÃO: O risco do ativo (%s) é superior ao seu perfil (%s). Deseja continuar? (S/N): ",
                    ativo.getRisco(), inv.getPerfilRisco());
            String resposta = scanner.nextLine().trim().toUpperCase();
            if (!"S".equals(resposta)) {
                System.out.println("Investimento cancelado pelo usuário.");
                return false;
            }
        }

        if (ativo.getRisco().equalsIgnoreCase("alto")
                && valor > inv.getCapitalDisponivel() * 0.8) {
            System.out.println("Não é permitido investir mais de 80% do capital disponível em ativos de risco alto.");
            return false;
        }

        if (valor > inv.getCapitalDisponivel()) {
            System.out.println("Capital insuficiente para realizar o investimento.");
            return false;
        }

        double valorInicialUnitario = ativo.getValorAtual();

        Investimento investimento = new Investimento(ativo, valor, valorInicialUnitario, 0);
        inv.getHistoricoInvestimentos().add(investimento);

        inv.setCapitalDisponivel(inv.getCapitalDisponivel() - valor);

        Transacao transacao = new Transacao(inv, ativo, valor, "COMPRA", LocalDate.now().toString());
        sistemaTransacoes.registrar(transacao);

        System.out.println("Investimento realizado com sucesso!");
        return true;
    }

    public boolean resgatarInvestimento(Investidor inv, Ativo ativo, double valorResgatar, SistemaTransacoes sistemaTransacoes) {
        if (valorResgatar <= 0) {
            System.out.println("O valor do resgate deve ser maior que zero.");
            return false;
        }

        DLL<Investimento> historico = inv.getHistoricoInvestimentos();
        if (historico.isEmpty()) {
            System.out.println("O investidor não possui investimentos.");
            return false;
        }

        Node<Investimento> atual = historico.getHead();

        while (atual != null) {
            Investimento investimento = atual.getData();

            if (investimento.getAtivo().equals(ativo)) {
                if (valorResgatar > investimento.getValorAplicado()) {
                    System.out.println("Valor de resgate maior que o valor ainda aplicado nesse ativo.");
                    return false;
                }

                double valorInicialUnitario = investimento.getValorInicial();
                double valorAtualUnitario = ativo.getValorAtual();

                if (valorInicialUnitario <= 0) {
                    System.out.println("Valor inicial inválido para o investimento.");
                    return false;
                }

                double fatorPreco = valorAtualUnitario / valorInicialUnitario;
                double variacaoPercentual = (fatorPreco - 1.0) * 100.0;

                double lucroPrejuizo = valorResgatar * (fatorPreco - 1.0);

                investimento.setValorAplicado(investimento.getValorAplicado() - valorResgatar);
                investimento.setLucroPrejuizoAcumulado(
                        investimento.getLucroPrejuizoAcumulado() + lucroPrejuizo
                );

                inv.setCapitalDisponivel(inv.getCapitalDisponivel() + valorResgatar + lucroPrejuizo);

                Transacao transacao = new Transacao(inv, ativo, valorResgatar, "RESGATE", LocalDate.now().toString());
                sistemaTransacoes.registrar(transacao);

                System.out.printf(
                        "Resgate realizado com sucesso! Variação do ativo: %.2f%% | Lucro/Prejuízo deste resgate: R$ %.2f%n",
                        variacaoPercentual, lucroPrejuizo);

                return true;
            }

            atual = atual.getNext();
        }

        System.out.println("Investimento não encontrado para esse ativo.");
        return false;
    }

    public DLL<Investidor> getListaInvestidores() {
        return listaInvestidores;
    }

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

            double saldoPrincipal = invst.getValorAplicado();
            double valorInicialUnitario = invst.getValorInicial();
            double valorAtualUnitario = invst.getAtivo().getValorAtual();

            double fatorPreco = 0.0;
            if (valorInicialUnitario > 0) {
                fatorPreco = valorAtualUnitario / valorInicialUnitario;
            }

            double variacaoPercentual = (fatorPreco - 1.0) * 100.0;
            double valorMercado = saldoPrincipal * fatorPreco;
            double lucroPrejuizoPotencial = valorMercado - saldoPrincipal;

            System.out.printf(
                    "%d) Ativo: %s | Saldo aplicado (principal): R$ %.2f | Valor de mercado: R$ %.2f | Variação: %.2f%% | Lucro/Prejuízo potencial: R$ %.2f | Lucro/Prejuízo realizado: R$ %.2f%n",
                    i++,
                    invst.getAtivo().getNome(),
                    saldoPrincipal,
                    valorMercado,
                    variacaoPercentual,
                    lucroPrejuizoPotencial,
                    invst.getLucroPrejuizoAcumulado()
            );

            atual = atual.getNext();
        }
    }

    private Investimento buscarInvestimento(Investidor inv, Ativo ativo) {
        DLL<Investimento> historico = inv.getHistoricoInvestimentos();
        Node<Investimento> atual = historico.getHead();

        while (atual != null) {
            Investimento investimento = atual.getData();
            if (investimento.getAtivo().equals(ativo)) {
                return investimento;
            }
            atual = atual.getNext();
        }
        return null;
    }

    public void menuInvestidores(Scanner scanner, SistemaAtivos sistemaAtivos, SistemaTransacoes sistemaTransacoes) {
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

                case 2:
                    if (listaInvestidores.isEmpty()) {
                        System.out.println("Nenhum investidor cadastrado.");
                        break;
                    }

                    Node<Investidor> atual = listaInvestidores.getHead();
                    int i = 1;

                    while (atual != null) {
                        Investidor invLista = atual.getData();
                        System.out.printf("%d) %s - Idade: %s - Perfil: %s - Capital: %.2f%n",
                                i++,
                                invLista.getNome(),
                                invLista.getIdade(),
                                invLista.getPerfilRisco(),
                                invLista.getCapitalDisponivel());
                        atual = atual.getNext();
                    }
                    break;

                case 3:
                    if (listaInvestidores.isEmpty()) {
                        System.out.println("Nenhum investidor cadastrado.");
                        break;
                    }

                    System.out.println("\nLista de Investidores:");
                    Node<Investidor> atualInv = listaInvestidores.getHead();
                    int idx = 1;

                    while (atualInv != null) {
                        Investidor invItem = atualInv.getData();
                        System.out.printf("%d) %s - Capital Disponível: %.2f%n",
                                idx++, invItem.getNome(), invItem.getCapitalDisponivel());
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

                    fazerInvestimento(investidorSelecionado, ativoSelecionado, valorInvestir, sistemaTransacoes, scanner);
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

                case 5:
                    if (listaInvestidores.isEmpty()) {
                        System.out.println("Nenhum investidor cadastrado.");
                        break;
                    }

                    System.out.println("\nLista de Investidores:");
                    Node<Investidor> atualVendaInv = listaInvestidores.getHead();
                    int idxVenda = 1;
                    while (atualVendaInv != null) {
                        Investidor invVenda = atualVendaInv.getData();
                        System.out.printf("%d) %s - Capital Disponível: %.2f%n",
                                idxVenda++, invVenda.getNome(), invVenda.getCapitalDisponivel());
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
                        Ativo aVenda = atualAtivoVenda.getData();
                        System.out.printf("%d) %s - %s - Risco: %s - Valor Atual: %.2f%n",
                                jVenda++, aVenda.getCodigo(), aVenda.getNome(), aVenda.getRisco(), aVenda.getValorAtual());
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

                    Investimento investimentoVenda = buscarInvestimento(investidorSelecionadoVenda, ativoSelecionadoVenda);
                    if (investimentoVenda == null) {
                        System.out.println("Este investidor não possui investimento neste ativo.");
                        break;
                    }

                    double saldoPrincipal = investimentoVenda.getValorAplicado();
                    double valorInicialUnitario = investimentoVenda.getValorInicial();
                    double valorAtualUnitario = ativoSelecionadoVenda.getValorAtual();

                    if (valorInicialUnitario <= 0) {
                        System.out.println("Valor inicial inválido para o investimento.");
                        break;
                    }

                    double fatorPreco = valorAtualUnitario / valorInicialUnitario;
                    double valorMercadoDisponivel = saldoPrincipal * fatorPreco;

                    System.out.printf(
                            "Saldo aplicado (principal): R$ %.2f | Valor de mercado disponível para resgate: R$ %.2f%n",
                            saldoPrincipal, valorMercadoDisponivel
                    );

                    System.out.println("1) Resgatar TODO o investimento");
                    System.out.println("2) Resgatar parte do principal");
                    System.out.print("Escolha uma opção: ");

                    int opcResgate;
                    while (!scanner.hasNextInt()) {
                        System.out.println("Digite um número válido.");
                        scanner.next();
                    }
                    opcResgate = scanner.nextInt();
                    scanner.nextLine();

                    double valorResgatar;
                    if (opcResgate == 1) {
                        valorResgatar = saldoPrincipal;
                    } else if (opcResgate == 2) {
                        while (true) {
                            System.out.print("Valor (principal) a resgatar (até " + saldoPrincipal + "): ");
                            try {
                                valorResgatar = Double.parseDouble(scanner.nextLine());
                                if (valorResgatar <= 0) {
                                    System.out.println("O valor deve ser maior que zero.");
                                    continue;
                                }
                                if (valorResgatar > saldoPrincipal) {
                                    System.out.println("Não pode resgatar mais do que o saldo aplicado.");
                                    continue;
                                }
                                break;
                            } catch (NumberFormatException e) {
                                System.out.println("Digite um valor numérico válido.");
                            }
                        }
                    } else {
                        System.out.println("Opção de resgate inválida.");
                        break;
                    }

                    resgatarInvestimento(investidorSelecionadoVenda, ativoSelecionadoVenda, valorResgatar, sistemaTransacoes);
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Opção inválida.");
            }

        } while (opcInvestidor != 0);
    }
}
