package a06.lojaonline;

public class EntregaExpressa extends ModalidadeEntrega {

    public EntregaExpressa() {
        super("Entrega expressa");
    }

    @Override
    public double calcularFrete(double subtotal) {
        return 20.0;
    }
}
