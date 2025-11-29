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
            System.out.println("0. Voltar");

            opcAtivo = Utils.lerOpcao(scanner, 0, 4);

            switch (opcAtivo) {

                case 1:
                    cadastrarAtivoMenu(scanner);
                    break;

                case 2:
                    listarAtivos();
                    break;

                case 3:
                    buscarPorNome(scanner);
                    break;

                case 4:
                    buscarPorCodigo(scanner);
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Opção inválida.");
            }

        } while (opcAtivo != 0);
    }

    private void cadastrarAtivoMenu(Scanner scanner) {

        System.out.print("Código: ");
        String codigo = Utils.lerTexto(scanner);

        System.out.print("Nome: ");
        String nome = Utils.lerTexto(scanner);

        // ---------------------
        // VALIDAÇÃO DO TIPO COM A/B/C
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
                case "A":
                    tipo = "Ação";
                    break;
                case "B":
                    tipo = "Renda Fixa";
                    break;
                case "C":
                    tipo = "Cripto";
                    break;
                default:
                    System.out.println("ERRO: Opção inválida! Digite A, B ou C.");
                    continue;
            }
            break;
        }

        // ---------------------
        // VALIDAÇÃO DO RISCO COM A/B/C
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
                case "A":
                    risco = "baixo";
                    break;
                case "B":
                    risco = "medio";
                    break;
                case "C":
                    risco = "alto";
                    break;
                default:
                    System.out.println("ERRO: Opção inválida! Digite A, B ou C.");
                    continue;
            }
            break;
        }

        // ---------------------
        // RENTABILIDADE MÉDIA (não aceita negativo)
        // ---------------------
        double rentabilidadeMedia;
        while (true) {
            System.out.print("Rentabilidade Média: ");
            rentabilidadeMedia = Utils.lerDouble(scanner);

            if (rentabilidadeMedia <= 0) {
                System.out.println("ERRO: A rentabilidade média NÃO pode ser negativa!");
            } else {
                break;
            }
        }

        // ---------------------
        // VALOR ATUAL (não aceita negativo)
        // ---------------------
        double valorAtual;
        while (true) {
            System.out.print("Valor Atual: ");
            valorAtual = Utils.lerDouble(scanner);

            if (valorAtual <= 0) {
                System.out.println("ERRO: O valor atual NÃO pode ser negativo!");
            } else {
                break;
            }
        }

        // ---------------------
        // VARIAÇÃO PERCENTUAL (aceita negativos)
        // ---------------------
        double variacaoPercentual;
        while (true) {
            System.out.print("Variação Percentual (%): ");
            variacaoPercentual = Utils.lerDouble(scanner);
            break; // Aceita valores negativos, 0 e positivos
        }

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
            System.out.printf("%d) Código: %s - Nome: %s - Tipo: %s - Risco: %s - Valor Atual: %.2f%n",
                    i++, a.getCodigo(), a.getNome(), a.getTipo(), a.getRisco(), a.getValorAtual());
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
}
