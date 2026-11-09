package a06.lojaonline;

public abstract class ModalidadeEntrega {

    private String nome;

    protected ModalidadeEntrega(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public abstract double calcularFrete(double subtotal);
}
