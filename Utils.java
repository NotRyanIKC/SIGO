import java.util.Scanner;

public class Utils {

    public static int lerOpcao(Scanner sc, int min, int max) {
        while (true) {
            System.out.print("Opção: ");
            String input = sc.nextLine().trim();

            if (!input.matches("\\d+")) {
                System.out.println("Erro: digite apenas números.");
                continue;
            }

            int valor = Integer.parseInt(input);

            if (valor < min || valor > max) {
                System.out.println("Erro: digite um valor entre " + min + " e " + max + ".");
                continue;
            }

            return valor;
        }
    }

    public static int lerInt(Scanner sc) {
        while (true) {
            String input = sc.nextLine().trim();
            if (!input.matches("\\d+")) {
                System.out.println("Erro: digite um número inteiro.");
                continue;
            }
            return Integer.parseInt(input);
        }
    }

    public static double lerDouble(Scanner sc) {
        while (true) {
            String input = sc.nextLine().replace(",", ".").trim();
            if (!input.matches("[-+]?[0-9]*\\.?[0-9]+")) {
                System.out.println("Erro: digite um número válido.");
                continue;
            }
            return Double.parseDouble(input);
        }
    }

    public static String lerTexto(Scanner sc) {
        while (true) {
            String txt = sc.nextLine().trim();
            if (txt.isEmpty()) {
                System.out.println("Erro: o campo não pode estar vazio.");
                continue;
            }
            return txt;
        }
    }
}
