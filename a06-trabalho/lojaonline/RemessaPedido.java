package a06.lojaonline;

public class RemessaPedido implements Rastreavel {

    private final String codigoRastreio;
    private final Pedido pedido;
    private StatusRastreamento status;

    public RemessaPedido(String codigoRastreio, Pedido pedido) {
        if (codigoRastreio == null || codigoRastreio.trim().isEmpty()) {
            throw new IllegalArgumentException("Código de rastreio é obrigatório.");
        }
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não pode ser nulo.");
        }
        if (pedido.contarItens() == 0) {
            throw new IllegalArgumentException("Pedido precisa ter pelo menos um item.");
        }
        this.codigoRastreio = codigoRastreio.trim();
        this.pedido = pedido;
        this.status = StatusRastreamento.PREPARANDO;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void atualizarStatus(String statusRastreamento) {
        StatusRastreamento novo = StatusRastreamento.converter(statusRastreamento);
        if (!status.podeAvancarPara(novo)) {
            throw new IllegalStateException("Transição inválida: " + status + " -> " + novo);
        }
        this.status = novo;
    }

    @Override
    public String getCodigoRastreio() {
        return codigoRastreio;
    }

    @Override
    public StatusRastreamento getStatus() {
        return status;
    }

    @Override
    public String consultarRastreamento() {
        switch (status) {
            case PREPARANDO:
                return "Remessa em preparação";
            case EM_TRANSITO:
                return "Remessa em trânsito";
            default:
                return "Remessa entregue ao cliente";
        }
    }
}
