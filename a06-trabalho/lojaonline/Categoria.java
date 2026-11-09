package a06.lojaonline;

public class Categoria {

    private int codigo;
    private String nome;

    public Categoria(int codigo, String nome) {
        if (codigo <= 0) {
            throw new IllegalArgumentException("Código da categoria deve ser positivo.");
        }
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da categoria é obrigatório.");
        }
        this.codigo = codigo;
        this.nome = nome.trim();
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }
}
