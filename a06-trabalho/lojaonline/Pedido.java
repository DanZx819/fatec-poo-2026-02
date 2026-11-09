package a06.lojaonline;

import java.util.ArrayList;

public class Pedido {

    private ArrayList<ItemPedido> itens = new ArrayList<>();

    public Pedido() {
    }

    public void adicionarProduto(Produto produto) {
        adicionarProduto(produto, 1);
    }

    public void adicionarProduto(Produto produto, int quantidade) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo.");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero.");
        }
        for (ItemPedido item : itens) {
            if (item.getProduto().getCodigo() == produto.getCodigo()) {
                item.aumentarQuantidade(quantidade);
                return;
            }
        }
        itens.add(new ItemPedido(produto, quantidade));
    }

    public double calcularSubtotal() {
        double subtotal = 0.0;
        for (ItemPedido item : itens) {
            subtotal += item.calcularSubtotal();
        }
        return subtotal;
    }

    public double calcularTotal(ModalidadeEntrega modalidade) {
        if (itens.isEmpty()) {
            throw new IllegalStateException("Pedido vazio não possui total.");
        }
        if (modalidade == null) {
            throw new IllegalArgumentException("Modalidade de entrega não pode ser nula.");
        }
        double subtotal = calcularSubtotal();
        return subtotal + modalidade.calcularFrete(subtotal);
    }

    public int contarItens() {
        return itens.size();
    }
}
