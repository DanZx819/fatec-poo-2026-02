package a06.lojaonline;

public class EntregaEconomica extends ModalidadeEntrega {

    public EntregaEconomica() {
        super("Entrega econômica");
    }

    @Override
    public double calcularFrete(double subtotal) {
        if (subtotal < 200.0) {
            return 10.0;
        }
        return 0.0;
    }
}
