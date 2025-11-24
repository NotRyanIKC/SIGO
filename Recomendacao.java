public class Recomendacao {

    private Ativo ativo;
    private String score;
    private String justificativa;
    private boolean ativa = true;


    public Recomendacao(Ativo ativo, String score, String justificativa, boolean ativa) {
        this.ativo = ativo;
        this.score = score;
        this.justificativa = justificativa;
        this.ativa = ativa;
    }

    public Ativo getAtivo() {
        return ativo;
    }

    public void setAtivo(Ativo ativo) {
        this.ativo = ativo;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }
    
}