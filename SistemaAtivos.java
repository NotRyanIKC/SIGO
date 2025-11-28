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
            System.out.println("\nMenu Ativos:");
            System.out.println("1. Cadastrar Ativo");
            System.out.println("2. Listar Ativos");
            System.out.println("3. Buscar Ativo por Nome");
            System.out.println("4. Buscar Ativo por Codigo");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opcao: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Digite um numero valido.");
                scanner.next();
            }
            opcAtivo = scanner.nextInt();
            scanner.nextLine();

            switch (opcAtivo) {
                case 1:
                    try {
                        System.out.print("Codigo: ");
                        String codigo = scanner.nextLine();
                        System.out.print("Nome: ");
                        String nome = scanner.nextLine();
                        System.out.print("Tipo: ");
                        String tipo = scanner.nextLine();
                        System.out.print("Risco: ");
                        String risco = scanner.nextLine();
                        System.out.print("Rentabilidade Media: ");
                        double rentabilidadeMedia = scanner.nextDouble();
                        System.out.print("Valor Atual: ");
                        double valorAtual = scanner.nextDouble();
                        System.out.print("Variacao Percentual: ");
                        double variacaoPercentual = scanner.nextDouble();
                        scanner.nextLine();

                        Ativo ativo = new Ativo(codigo, nome, tipo, risco, rentabilidadeMedia, valorAtual, variacaoPercentual, 0);
                        cadastrarAtivo(ativo);
                        System.out.println("Ativo cadastrado com sucesso!");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Erro ao cadastrar ativo: " + e.getMessage());
                    }
                    break;

                case 2:
                    if (listaAtivos.isEmpty()) {
                        System.out.println("Nenhum ativo cadastrado.");
                    } else {
                        Node<Ativo> atual = listaAtivos.getHead();
                        int i = 1;
                        while (atual != null) {
                            Ativo atv = atual.getData();
                            System.out.printf("%d) Codigo: %s - Nome: %s - Tipo: %s - Risco: %s - Valor Atual: %.2f%n",
                                    i++, atv.getCodigo(), atv.getNome(), atv.getTipo(), atv.getRisco(), atv.getValorAtual());
                            atual = atual.getNext();
                        }
                    }
                    break;

                case 3:
                    System.out.print("Digite o nome do ativo: ");
                    String nomeBusca = scanner.nextLine();
                    Ativo encontradoNome = new BuscaAtivos().buscaLinear(listaAtivos, nomeBusca);
                    if (encontradoNome != null) {
                        System.out.printf("Ativo encontrado: Codigo: %s - Nome: %s - Tipo: %s - Valor Atual: %.2f%n",
                                encontradoNome.getCodigo(), encontradoNome.getNome(), encontradoNome.getTipo(), encontradoNome.getValorAtual());
                    } else {
                        System.out.println("Ativo nao encontrado.");
                    }
                    break;

                case 4:
                    System.out.print("Digite o codigo do ativo: ");
                    String codigoBusca = scanner.nextLine();
                    Ativo encontradoCodigo = new BuscaAtivos().buscaBinaria(listaAtivos, codigoBusca);
                    if (encontradoCodigo != null) {
                        System.out.printf("Ativo encontrado: Codigo: %s - Nome: %s - Tipo: %s - Valor Atual: %.2f%n",
                                encontradoCodigo.getCodigo(), encontradoCodigo.getNome(), encontradoCodigo.getTipo(), encontradoCodigo.getValorAtual());
                    } else {
                        System.out.println("Ativo nao encontrado.");
                    }
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Opcao invalida. Tente novamente.");
            }
        } while (opcAtivo != 0);
    }
}
