public class Investidor {
    private String nome;
    private String idade;
    private String perfilRisco;
    private double capitalDisponivel;
    private DLL<Investimento> historicoInvestimentos;

    public Investidor(String nome, String idade, String perfilRisco, double capitalDisponivel) {
        if (capitalDisponivel < 0) {
            throw new IllegalArgumentException("O capital disponível não pode ser negativo.");
        }
        this.nome = nome;
        this.idade = idade;
        this.perfilRisco = perfilRisco;
        this.capitalDisponivel = capitalDisponivel;
        this.historicoInvestimentos = new DLL<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIdade() {
        return idade;
    }

    public void setIdade(String idade) {
        this.idade = idade;
    }

    public String getPerfilRisco() {
        return perfilRisco;
    }

    public void setPerfilRisco(String perfilRisco) {
        this.perfilRisco = perfilRisco;
    }

    public double getCapitalDisponivel() {
        return capitalDisponivel;
    }

    public void setCapitalDisponivel(double capitalDisponivel) {
        if (capitalDisponivel < 0) {
            throw new IllegalArgumentException("O capital disponível não pode ser negativo.");
        }
        this.capitalDisponivel = capitalDisponivel;
    }

    public DLL<Investimento> getHistoricoInvestimentos() {
        return historicoInvestimentos;
    }

    public void setHistoricoInvestimentos(DLL<Investimento> historicoInvestimentos) {
        this.historicoInvestimentos = historicoInvestimentos;
    }

    public double calcularTotalInvestido() {
        double total = 0.0;
        Node<Investimento> atual = historicoInvestimentos.getHead();
        while (atual != null) {
            total += atual.getData().getValorAplicado();
            atual = atual.getNext();
        }
        return total;
    }

    public double calcularLucroPrejuizoAcumulado() {
        double total = 0.0;
        Node<Investimento> atual = historicoInvestimentos.getHead();
        while (atual != null) {
            total += atual.getData().getLucroPrejuizoAcumulado();
            atual = atual.getNext();
        }
        return total;
    }
}
