package a06.lojaonline;

public class DevolucaoPedido implements Rastreavel {

    private final String codigoRastreio;
    private final RemessaPedido remessaOriginal;
    private final String motivo;
    private StatusRastreamento status;

    public DevolucaoPedido(String codigoRastreio, RemessaPedido remessaOriginal, String motivo) {
        if (codigoRastreio == null || codigoRastreio.trim().isEmpty()) {
            throw new IllegalArgumentException("Código de rastreio é obrigatório.");
        }
        if (remessaOriginal == null) {
            throw new IllegalArgumentException("Remessa original não pode ser nula.");
        }
        if (remessaOriginal.getStatus() != StatusRastreamento.CONCLUIDO) {
            throw new IllegalStateException("Remessa original ainda não foi entregue.");
        }
        if (motivo == null || motivo.trim().isEmpty()) {
            throw new IllegalArgumentException("Motivo é obrigatório.");
        }
        this.codigoRastreio = codigoRastreio.trim();
        this.remessaOriginal = remessaOriginal;
        this.motivo = motivo.trim();
        this.status = StatusRastreamento.PREPARANDO;
    }

    public RemessaPedido getRemessaOriginal() {
        return remessaOriginal;
    }

    public String getMotivo() {
        return motivo;
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
                return "Devolução em preparação";
            case EM_TRANSITO:
                return "Devolução em trânsito";
            default:
                return "Devolução recebida pela loja";
        }
    }
}
