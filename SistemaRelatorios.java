import java.util.Scanner;

public class SistemaRelatorios {
    public void top5Rentabilidade(DLL<Ativo> listaAtivos) {
        long inicio = System.nanoTime();
        if (listaAtivos == null || listaAtivos.isEmpty()) {
            System.out.println("Nenhum ativo cadastrado.");
            return;
        }

        Ativo[] top = new Ativo[5];
        Node<Ativo> atual = listaAtivos.getHead();

        while (atual != null) {
            Ativo a = atual.getData();
            for (int i = 0; i < top.length; i++) {
                if (top[i] == null || a.getRentabilidadeMedia() > top[i].getRentabilidadeMedia()) {
                    for (int j = top.length - 1; j > i; j--) {
                        top[j] = top[j - 1];
                    }
                    top[i] = a;
                    break;
                }
            }
            atual = atual.getNext();
        }

        System.out.println("Top 5 ativos por rentabilidade:");
        for (int i = 0; i < top.length; i++) {
            if (top[i] != null) {
                Ativo a = top[i];
                System.out.printf("%d) %s - %s - Rentabilidade: %.2f%%%n",
                        i + 1, a.getCodigo(), a.getNome(), a.getRentabilidadeMedia());
            }
        }
        long fim = System.nanoTime();
        System.out.printf("Relatório gerado em %d ns.%n", (fim - inicio));
    }

    private double scoreRiscoRetorno(Ativo a) {
        String risco = a.getRisco() == null ? "" : a.getRisco().toLowerCase();
        double fator;
        if (risco.equals("baixo")) {
            fator = 1.0;
        } else if (risco.equals("moderado")) {
            fator = 1.3;
        } else if (risco.equals("alto")) {
            fator = 1.6;
        } else {
            fator = 1.0;
        }
        return a.getRentabilidadeMedia() / fator;
    }

    public void top5RiscoRetorno(DLL<Ativo> listaAtivos) {
        long inicio = System.nanoTime();
        if (listaAtivos == null || listaAtivos.isEmpty()) {
            System.out.println("Nenhum ativo cadastrado.");
            return;
        }

        Ativo[] top = new Ativo[5];
        Double[] scores = new Double[5];
        Node<Ativo> atual = listaAtivos.getHead();

        while (atual != null) {
            Ativo a = atual.getData();
            double score = scoreRiscoRetorno(a);
            for (int i = 0; i < top.length; i++) {
                if (top[i] == null || score > scores[i]) {
                    for (int j = top.length - 1; j > i; j--) {
                        top[j] = top[j - 1];
                        scores[j] = scores[j - 1];
                    }
                    top[i] = a;
                    scores[i] = score;
                    break;
                }
            }
            atual = atual.getNext();
        }

        System.out.println("Top 5 ativos por relacao risco x retorno:");
        for (int i = 0; i < top.length; i++) {
            if (top[i] != null) {
                Ativo a = top[i];
                double score = scores[i];
                System.out.printf("%d) %s - %s - Risco: %s - Rentabilidade: %.2f%% - Score: %.2f%n",
                        i + 1, a.getCodigo(), a.getNome(), a.getRisco(), a.getRentabilidadeMedia(), score);
            }
        }
        long fim = System.nanoTime();
        System.out.printf("Relatório gerado em %d ns.%n", (fim - inicio));
    }

    public void relatorioInvestidor(Investidor inv) {
        System.out.println("Relatorio do investidor: " + inv.getNome());
        System.out.println("Idade: " + inv.getIdade());
        System.out.println("Perfil de risco: " + inv.getPerfilRisco());
        System.out.printf("Capital disponivel: %.2f%n", inv.getCapitalDisponivel());

        DLL<Investimento> hist = inv.getHistoricoInvestimentos();
        if (hist.isEmpty()) {
            System.out.println("Nenhum investimento registrado.");
            return;
        }

        Node<Investimento> atual = hist.getHead();
        double totalAplicado = 0.0;
        double totalAtual = 0.0;

        String[] tipos = new String[50];
        double[] valoresPorTipo = new double[50];
        int countTipos = 0;

        while (atual != null) {
            Investimento investimento = atual.getData();
            Ativo a = investimento.getAtivo();
            double valorAplicado = investimento.getValorAplicado();
            double valorAtual = a.getValorAtual() * (valorAplicado / investimento.getValorInicial());
            double lucroAtual = valorAtual - valorAplicado;

            totalAplicado += valorAplicado;
            totalAtual += valorAtual;

            System.out.printf("Ativo: %s - %s | Tipo: %s | Valor aplicado: %.2f | Valor atual estimado: %.2f | Lucro/Prejuizo: %.2f%n",
                    a.getCodigo(), a.getNome(), a.getTipo(), valorAplicado, valorAtual, lucroAtual);

            String tipo = a.getTipo();
            int idx = -1;
            for (int i = 0; i < countTipos; i++) {
                if (tipos[i].equalsIgnoreCase(tipo)) {
                    idx = i;
                    break;
                }
            }
            if (idx == -1) {
                tipos[countTipos] = tipo;
                valoresPorTipo[countTipos] = valorAplicado;
                countTipos++;
            } else {
                valoresPorTipo[idx] += valorAplicado;
            }

            atual = atual.getNext();
        }

        double lucroTotal = totalAtual - totalAplicado;
        System.out.printf("Total aplicado: %.2f%n", totalAplicado);
        System.out.printf("Valor atual estimado: %.2f%n", totalAtual);
        System.out.printf("Lucro/Prejuizo total estimado: %.2f%n", lucroTotal);

        System.out.println("Distribuicao do portfólio por tipo de ativo:");
        for (int i = 0; i < countTipos; i++) {
            double perc = (valoresPorTipo[i] / totalAplicado) * 100.0;
            System.out.printf("%s: %.2f%% (%.2f)%n", tipos[i], perc, valoresPorTipo[i]);
        }
    }

    public void menuRelatorios(Scanner scanner, SistemaAtivos sistemaAtivos, SistemaInvestidores sistemaInvestidores) {
        int opcRelatorio;
        do {
            System.out.println("\nMenu Relatorios:");
            System.out.println("1. Top 5 Ativos por Rentabilidade");
            System.out.println("2. Top 5 Ativos por Risco x Retorno");
            System.out.println("3. Relatorio de Investidor");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opcao: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Digite um numero valido.");
                scanner.next();
            }
            opcRelatorio = scanner.nextInt();
            scanner.nextLine();

            switch (opcRelatorio) {
                case 1:
                    top5Rentabilidade(sistemaAtivos.getListaAtivos());
                    break;

                case 2:
                    top5RiscoRetorno(sistemaAtivos.getListaAtivos());
                    break;

                case 3:
                    System.out.println("Lista de Investidores:");
                    DLL<Investidor> listaInvestidores = sistemaInvestidores.getListaInvestidores();
                    if (listaInvestidores.isEmpty()) {
                        System.out.println("Nenhum investidor cadastrado.");
                        break;
                    }
                    Node<Investidor> atual = listaInvestidores.getHead();
                    int i = 1;
                    while (atual != null) {
                        System.out.printf("%d) %s%n", i++, atual.getData().getNome());
                        atual = atual.getNext();
                    }
                    System.out.print("Escolha o numero do investidor: ");
                    int investidorIndex = scanner.nextInt() - 1;
                    scanner.nextLine();
                    if (investidorIndex < 0 || investidorIndex >= listaInvestidores.size()) {
                        System.out.println("Investidor invalido.");
                        break;
                    }
                    Investidor investidorSelecionado = listaInvestidores.get(investidorIndex);
                    relatorioInvestidor(investidorSelecionado);
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Opcao invalida. Tente novamente.");
            }
        } while (opcRelatorio != 0);
    }
}
