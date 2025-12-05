import java.util.Scanner;

public class SistemaRecomendacoes {
    private DLL<Recomendacao> listaRecomendacoes;

    public SistemaRecomendacoes() {
        this.listaRecomendacoes = new DLL<>();
    }

    public DLL<Recomendacao> getListaRecomendacoes() {
        return listaRecomendacoes;
    }

    public void adicionarRecomendacao(Recomendacao recomendacao) {
        listaRecomendacoes.add(recomendacao);
    }

    // ----------------------------------------------------
    // FUNÇÕES AUXILIARES DE RISCO E PERFIL
    // ----------------------------------------------------
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

    private boolean riscoCompativel(Investidor investidor, Ativo ativo) {
        int nPerfil = nivelRiscoPerfil(investidor.getPerfilRisco());
        int nAtivo  = nivelRiscoAtivo(ativo.getRisco());
        return nAtivo <= nPerfil; // ativo não pode ser mais arriscado que o perfil
    }

    private double mediaRentabilidade(DLL<Ativo> listaAtivos) {
        if (listaAtivos == null || listaAtivos.isEmpty()) return 0.0;
        double soma = 0.0;
        int cont = 0;
        Node<Ativo> node = listaAtivos.getHead();
        while (node != null) {
            soma += node.getData().getRentabilidadeMedia();
            cont++;
            node = node.getNext();
        }
        return cont > 0 ? soma / cont : 0.0;
    }

    // score heurístico (IA simbólica)
    private double scoreHeuristico(Investidor inv, Ativo ativo, double mediaRentab) {
        double score = 0.0;

        double rent = ativo.getRentabilidadeMedia();
        double bonusRent = rent - mediaRentab;
        score += rent + bonusRent;

        int nPerfil = nivelRiscoPerfil(inv.getPerfilRisco());
        int nAtivo  = nivelRiscoAtivo(ativo.getRisco());
        if (nAtivo == nPerfil) {
            score += 5.0;
        } else if (nAtivo < nPerfil) {
            score += 2.0;
        } else {
            score -= 5.0;
        }

        double vol = Math.abs(ativo.getVariacaoPercentual());
        String perfil = inv.getPerfilRisco().toLowerCase();
        if (perfil.equals("conservador")) {
            score += Math.max(0, 20.0 - vol);
        } else if (perfil.equals("moderado")) {
            double dist = Math.abs(vol - 20.0);
            score += Math.max(0, 20.0 - dist);
        } else if (perfil.equals("arrojado")) {
            score += vol / 2.0;
        }

        return score;
    }

    // ----------------------------------------------------
    // MENU
    // ----------------------------------------------------
    public void menuRecomendacoes(Scanner scanner, DLL<Ativo> listaAtivos, DLL<Investidor> listaInvestidores) {
        int opcRecomendacao;
        do {
            System.out.println("\nMenu Recomendacoes:");
            System.out.println("1. Gerar Recomendacoes para um Investidor");
            System.out.println("2. Listar Recomendacoes");
            System.out.println("3. Sugerir Realocacao de Portfólio");
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
                    listarRecomendacoes();
                    break;

                case 3:
                    if (listaInvestidores.isEmpty()) {
                        System.out.println("Nenhum investidor cadastrado.");
                        break;
                    }
                    System.out.println("\nEscolha um investidor para realocacao:");
                    Node<Investidor> nodeInv2 = listaInvestidores.getHead();
                    int idx2 = 1;
                    while (nodeInv2 != null) {
                        Investidor inv = nodeInv2.getData();
                        System.out.printf("%d) %s%n", idx2++, inv.getNome());
                        nodeInv2 = nodeInv2.getNext();
                    }
                    System.out.print("Numero do investidor: ");
                    int sel2 = scanner.nextInt() - 1;
                    scanner.nextLine();
                    if (sel2 < 0 || sel2 >= listaInvestidores.size()) {
                        System.out.println("Investidor invalido.");
                        break;
                    }
                    Investidor investidorRealoc = listaInvestidores.get(sel2);
                    sugerirRealocacao(investidorRealoc, listaAtivos);
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Opcao invalida. Tente novamente.");
            }
        } while (opcRecomendacao != 0);
    }

    // ----------------------------------------------------
    // GERAÇÃO DE RECOMENDAÇÕES (regras 6, 7 e penalização)
    // ----------------------------------------------------
    public void gerarRecomendacoes(Investidor investidor, DLL<Ativo> listaAtivos) {
        System.out.println("\nRecomendacoes para o investidor " + investidor.getNome() + ":");

        if (listaAtivos == null || listaAtivos.isEmpty()) {
            System.out.println("Nenhum ativo cadastrado.");
            return;
        }

        // limpa recomendações anteriores
        listaRecomendacoes = new DLL<>();

        double mediaRent = mediaRentabilidade(listaAtivos);
        System.out.printf("Rentabilidade média do sistema: %.2f%%%n", mediaRent);

        int n = listaAtivos.size();
        Ativo[] candidatos = new Ativo[n];
        double[] scores = new double[n];
        int count = 0;

        Node<Ativo> atual = listaAtivos.getHead();
        while (atual != null) {
            Ativo ativo = atual.getData();

            // penalização de confiança: 3 ciclos negativos
            if (ativo.getCiclosNegativos() >= 3) {
                String justificativaSusp = String.format(
                        "Ativo %s (%s) suspenso: %d ciclos negativos consecutivos.",
                        ativo.getCodigo(), ativo.getNome(), ativo.getCiclosNegativos());
                Recomendacao recSusp = new Recomendacao(
                        ativo,
                        "0.00",
                        justificativaSusp,
                        false
                );
                adicionarRecomendacao(recSusp);
                atual = atual.getNext();
                continue;
            }

            // 1. Compatibilidade de risco
            if (!riscoCompativel(investidor, ativo)) {
                atual = atual.getNext();
                continue;
            }

            // 2. Rentabilidade acima da média
            if (ativo.getRentabilidadeMedia() < mediaRent) {
                atual = atual.getNext();
                continue;
            }

            // 3. Volatilidade controlada
            double vol = Math.abs(ativo.getVariacaoPercentual());
            double maxVol;
            String perfil = investidor.getPerfilRisco().toLowerCase();
            if (perfil.equals("conservador")) {
                maxVol = 15.0;
            } else if (perfil.equals("moderado")) {
                maxVol = 35.0;
            } else {
                maxVol = Double.MAX_VALUE;
            }
            if (vol > maxVol) {
                atual = atual.getNext();
                continue;
            }

            double score = scoreHeuristico(investidor, ativo, mediaRent);
            candidatos[count] = ativo;
            scores[count] = score;
            count++;

            atual = atual.getNext();
        }

        if (count == 0) {
            System.out.println("Nenhum ativo atende aos critérios de recomendação para este perfil.");
            return;
        }

        // ordena por score (decrescente) – insertion sort
        for (int i = 1; i < count; i++) {
            Ativo keyAtivo = candidatos[i];
            double keyScore = scores[i];
            int j = i - 1;
            while (j >= 0 && scores[j] < keyScore) {
                candidatos[j + 1] = candidatos[j];
                scores[j + 1] = scores[j];
                j--;
            }
            candidatos[j + 1] = keyAtivo;
            scores[j + 1] = keyScore;
        }

        // diversificação: não repetir TYPE consecutivo
        String ultimoTipo = null;
        int qtdSugeridos = 0;

        for (int i = 0; i < count; i++) {
            Ativo a = candidatos[i];
            if (a == null) continue;

            String tipo = a.getTipo();
            if (ultimoTipo != null && ultimoTipo.equalsIgnoreCase(tipo)) {
                continue;
            }
            ultimoTipo = tipo;
            qtdSugeridos++;

            double score = scores[i];

            String justificativa = String.format(
                    "Ativo recomendado: %s (%s) | Codigo: %s | Risco: %s | Rentabilidade: %.2f%% | Var. acumulada: %.2f%% | Score: %.2f",
                    a.getNome(), a.getTipo(), a.getCodigo(), a.getRisco(),
                    a.getRentabilidadeMedia(), a.getVariacaoPercentual(), score
            );

            System.out.println(justificativa);

            Recomendacao rec = new Recomendacao(
                    a,
                    String.format("%.2f", score),
                    justificativa,
                    true
            );
            adicionarRecomendacao(rec);
        }

        if (qtdSugeridos == 0) {
            System.out.println("Nenhum ativo pôde ser sugerido respeitando a regra de diversificação.");
        }
    }

    // ----------------------------------------------------
    // LISTAR RECOMENDAÇÕES
    // ----------------------------------------------------
    private void listarRecomendacoes() {
        if (listaRecomendacoes.isEmpty()) {
            System.out.println("Nenhuma recomendacao cadastrada.");
            return;
        }

        Node<Recomendacao> atual = listaRecomendacoes.getHead();
        int i = 1;
        while (atual != null) {
            Recomendacao r = atual.getData();
            String status = r.isAtiva() ? "ATIVA" : "SUSPENSA";
            System.out.printf(
                    "%d) [%s] Ativo: %s (%s) | Score: %s | %s%n",
                    i++,
                    status,
                    r.getAtivo().getCodigo(),
                    r.getAtivo().getNome(),
                    r.getScore(),
                    r.getJustificativa()
            );
            atual = atual.getNext();
        }
    }

    // ----------------------------------------------------
    // REALOCAÇÃO AUTOMÁTICA DE PORTFÓLIO (regra 8)
    // ----------------------------------------------------
    public void sugerirRealocacao(Investidor investidor, DLL<Ativo> listaAtivos) {
        DLL<Investimento> hist = investidor.getHistoricoInvestimentos();
        if (hist.isEmpty()) {
            System.out.println("Investidor não possui investimentos.");
            return;
        }

        System.out.println("\nSugestoes de realocacao para " + investidor.getNome() + ":");

        Node<Investimento> node = hist.getHead();
        boolean houveSugestao = false;

        while (node != null) {
            Investimento inv = node.getData();
            Ativo ativoAtual = inv.getAtivo();
            double principal = inv.getValorAplicado();

            if (principal <= 0) {
                node = node.getNext();
                continue;
            }

            double valorInicialUnitario = inv.getValorInicial();
            double valorAtualUnitario = ativoAtual.getValorAtual();
            if (valorInicialUnitario <= 0) {
                node = node.getNext();
                continue;
            }

            double fatorPreco = valorAtualUnitario / valorInicialUnitario;
            double valorMercado = principal * fatorPreco;
            double lucroPrejuizo = valorMercado - principal;

            // só olha ativos em prejuízo
            if (lucroPrejuizo >= 0) {
                node = node.getNext();
                continue;
            }

            // procura ativo substituto com MESMO risco e rentabilidade maior
            Ativo melhor = null;
            double melhorRent = ativoAtual.getRentabilidadeMedia();

            Node<Ativo> nodeA = listaAtivos.getHead();
            while (nodeA != null) {
                Ativo cand = nodeA.getData();

                if (cand == ativoAtual) {
                    nodeA = nodeA.getNext();
                    continue;
                }

                if (!cand.getRisco().equalsIgnoreCase(ativoAtual.getRisco())) {
                    nodeA = nodeA.getNext();
                    continue;
                }

                if (cand.getRentabilidadeMedia() <= melhorRent) {
                    nodeA = nodeA.getNext();
                    continue;
                }

                if (cand.getCiclosNegativos() >= 3) {
                    nodeA = nodeA.getNext();
                    continue;
                }

                melhor = cand;
                melhorRent = cand.getRentabilidadeMedia();
                nodeA = nodeA.getNext();
            }

            if (melhor != null) {
                houveSugestao = true;
                System.out.printf(
                        "Sugestao: realocar do ativo %s (%s) para %s (%s). Prejuizo atual: R$ %.2f | Rentab. atual: %.2f%% | Rentab. sugerida: %.2f%% | Risco: %s%n",
                        ativoAtual.getCodigo(), ativoAtual.getNome(),
                        melhor.getCodigo(), melhor.getNome(),
                        lucroPrejuizo,
                        ativoAtual.getRentabilidadeMedia(),
                        melhor.getRentabilidadeMedia(),
                        melhor.getRisco()
                );

                String justificativa = String.format(
                        "Realocar de %s para %s: ativo atual em prejuizo (R$ %.2f) e rentabilidade menor (%.2f%% < %.2f%%).",
                        ativoAtual.getCodigo(), melhor.getCodigo(),
                        lucroPrejuizo,
                        ativoAtual.getRentabilidadeMedia(),
                        melhor.getRentabilidadeMedia()
                );

                Recomendacao rec = new Recomendacao(
                        melhor,
                        String.format("%.2f", melhorRent),
                        justificativa,
                        true
                );
                adicionarRecomendacao(rec);
            } else {
                System.out.printf(
                        "Ativo %s (%s) está em prejuizo (R$ %.2f), mas não há outro ativo com mesmo risco e rentabilidade melhor para sugerir realocacao.%n",
                        ativoAtual.getCodigo(), ativoAtual.getNome(), lucroPrejuizo
                );
            }

            node = node.getNext();
        }

        if (!houveSugestao) {
            System.out.println("Nenhum ativo em prejuizo encontrou substituto adequado para realocacao.");
        }
    }
}
