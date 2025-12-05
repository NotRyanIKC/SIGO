import java.util.Random;
import java.util.Scanner;

public class SistemaAtivos {
    private DLL<Ativo> listaAtivos;

    public SistemaAtivos() {
        this.listaAtivos = new DLL<>();
    }

    public void cadastrarAtivo(Ativo ativo) {
        listaAtivos.add(ativo);
    }

    public DLL<Ativo> getListaAtivos() {
        return listaAtivos;
    }

    public void setListaAtivos(DLL<Ativo> listaAtivos) {
        this.listaAtivos = listaAtivos;
    }

    public void menuAtivos(Scanner scanner) {
        int opcAtivo;
        do {
            System.out.println("\nMENU ATIVOS:");
            System.out.println("1. Cadastrar Ativo");
            System.out.println("2. Listar Ativos");
            System.out.println("3. Buscar Ativo por Nome");
            System.out.println("4. Buscar Ativo por Código");
            System.out.println("5. Simular ciclo de mercado (atualizar valores)");
            System.out.println("0. Voltar");

            opcAtivo = Utils.lerOpcao(scanner, 0, 5);

            switch (opcAtivo) {
                case 1 -> cadastrarAtivoMenu(scanner);
                case 2 -> listarAtivos();
                case 3 -> buscarPorNome(scanner);
                case 4 -> buscarPorCodigo(scanner);
                case 5 -> simularCicloMercado();
                case 0 -> { }
                default -> System.out.println("Opção inválida.");
            }

        } while (opcAtivo != 0);
    }

    private void cadastrarAtivoMenu(Scanner scanner) {

        System.out.print("Código: ");
        String codigo = Utils.lerTexto(scanner);

        System.out.print("Nome: ");
        String nome = Utils.lerTexto(scanner);

        // ---------------------
        // TIPO
        // ---------------------
        String tipo = "";
        while (true) {
            System.out.println("\nSelecione o Tipo do Ativo:");
            System.out.println("A - Ação");
            System.out.println("B - Renda Fixa");
            System.out.println("C - Cripto");
            System.out.print("Opção: ");

            String opc = scanner.nextLine().trim().toUpperCase();

            switch (opc) {
                case "A" -> tipo = "Ação";
                case "B" -> tipo = "Renda Fixa";
                case "C" -> tipo = "Cripto";
                default -> {
                    System.out.println("ERRO: Opção inválida! Digite A, B ou C.");
                    continue;
                }
            }
            break;
        }

        // ---------------------
        // RISCO
        // ---------------------
        String risco = "";
        while (true) {
            System.out.println("\nSelecione o Risco:");
            System.out.println("A - Baixo");
            System.out.println("B - Médio");
            System.out.println("C - Alto");
            System.out.print("Opção: ");

            String opc = scanner.nextLine().trim().toUpperCase();

            switch (opc) {
                case "A" -> risco = "baixo";
                case "B" -> risco = "medio";
                case "C" -> risco = "alto";
                default -> {
                    System.out.println("ERRO: Opção inválida! Digite A, B ou C.");
                    continue;
                }
            }
            break;
        }

        // ---------------------
        // RENTABILIDADE MÉDIA
        // ---------------------
        double rentabilidadeMedia;
        while (true) {
            System.out.print("Rentabilidade Média (%): ");
            rentabilidadeMedia = Utils.lerDouble(scanner);

            if (rentabilidadeMedia <= 0) {
                System.out.println("ERRO: A rentabilidade média NÃO pode ser negativa ou zero!");
            } else {
                break;
            }
        }

        // ---------------------
        // VALOR ATUAL
        // ---------------------
        double valorAtual;
        while (true) {
            System.out.print("Valor Atual: ");
            valorAtual = Utils.lerDouble(scanner);

            if (valorAtual <= 0) {
                System.out.println("ERRO: O valor atual NÃO pode ser negativo ou zero!");
            } else {
                break;
            }
        }

        // ---------------------
        // VARIAÇÃO PERCENTUAL INICIAL (pode ser 0)
        // ---------------------
        System.out.print("Variação Percentual inicial (%): ");
        double variacaoPercentual = Utils.lerDouble(scanner);

        Ativo ativo = new Ativo(
                codigo, nome, tipo, risco,
                rentabilidadeMedia, valorAtual,
                variacaoPercentual, 0
        );

        cadastrarAtivo(ativo);
        System.out.println("Ativo cadastrado com sucesso!");
    }

    private void listarAtivos() {
        if (listaAtivos.isEmpty()) {
            System.out.println("Nenhum ativo cadastrado.");
            return;
        }

        Node<Ativo> atual = listaAtivos.getHead();
        int i = 1;

        while (atual != null) {
            Ativo a = atual.getData();
            System.out.printf(
                "%d) Código: %s - Nome: %s - Tipo: %s - Risco: %s - Valor Atual: %.2f - Var%% último ciclo: %.2f - Ciclos negativos: %d%n",
                i++, a.getCodigo(), a.getNome(), a.getTipo(), a.getRisco(),
                a.getValorAtual(), a.getVariacaoPercentual(), a.getCiclosNegativos()
            );
            atual = atual.getNext();
        }
    }

    private void buscarPorNome(Scanner scanner) {
        System.out.print("Digite o nome do ativo: ");
        String nomeBusca = Utils.lerTexto(scanner);

        Ativo encontrado = new BuscaAtivos().buscaLinear(listaAtivos, nomeBusca);

        if (encontrado != null) {
            System.out.printf("Ativo encontrado: Código: %s - Nome: %s - Tipo: %s - Valor Atual: %.2f%n",
                    encontrado.getCodigo(), encontrado.getNome(), encontrado.getTipo(), encontrado.getValorAtual());
        } else {
            System.out.println("Ativo NÃO encontrado.");
        }
    }

    private void buscarPorCodigo(Scanner scanner) {
        System.out.print("Digite o código do ativo: ");
        String codigoBusca = Utils.lerTexto(scanner);

        Ativo encontrado = new BuscaAtivos().buscaBinaria(listaAtivos, codigoBusca);

        if (encontrado != null) {
            System.out.printf("Ativo encontrado: Código: %s - Nome: %s - Tipo: %s - Valor Atual: %.2f%n",
                    encontrado.getCodigo(), encontrado.getNome(), encontrado.getTipo(), encontrado.getValorAtual());
        } else {
            System.out.println("Ativo NÃO encontrado.");
        }
    }

    /**
     * Simula um ciclo de mercado:
     * cada ativo recebe uma variação aleatória entre -10% e +10%.
     * Atualiza valorAtual, variacaoPercentual e ciclosNegativos.
     */
    private void simularCicloMercado() {
        if (listaAtivos.isEmpty()) {
            System.out.println("Nenhum ativo cadastrado.");
            return;
        }

        Random random = new Random();
        Node<Ativo> atual = listaAtivos.getHead();

        while (atual != null) {
            Ativo a = atual.getData();

            double variacao = -10.0 + 20.0 * random.nextDouble(); // -10% a +10%
            a.aplicarVariacaoPercentual(variacao);

            atual = atual.getNext();
        }

        System.out.println("Ciclo de mercado aplicado: valores e variacoes atualizados.");
    }
}
