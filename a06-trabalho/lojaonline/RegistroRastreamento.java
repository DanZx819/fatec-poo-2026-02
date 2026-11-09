package a06.lojaonline;

import java.util.ArrayList;

public class RegistroRastreamento {

    private ArrayList<RemessaPedido> remessas = new ArrayList<>();
    private ArrayList<DevolucaoPedido> devolucoes = new ArrayList<>();

    public RemessaPedido criarRemessa(String codigoRastreio, Pedido pedido, ModalidadeEntrega modalidade) {
        if (modalidade == null) {
            throw new IllegalArgumentException("Modalidade não pode ser nula.");
        }
        if (modalidade instanceof RetiradaNaLoja) {
            throw new IllegalStateException("Retirada na loja não gera remessa.");
        }
        RemessaPedido remessa = new RemessaPedido(codigoRastreio, pedido);
        verificarCodigoLivre(remessa.getCodigoRastreio());
        for (RemessaPedido existente : remessas) {
            if (existente.getPedido() == pedido) {
                throw new IllegalStateException("Pedido já possui remessa.");
            }
        }
        remessas.add(remessa);
        return remessa;
    }

    public DevolucaoPedido criarDevolucao(String codigoRastreio, RemessaPedido remessaOriginal, String motivo) {
        if (remessaOriginal != null && !remessas.contains(remessaOriginal)) {
            throw new IllegalArgumentException("Remessa não pertence a este registro.");
        }
        DevolucaoPedido devolucao = new DevolucaoPedido(codigoRastreio, remessaOriginal, motivo);
        verificarCodigoLivre(devolucao.getCodigoRastreio());
        for (DevolucaoPedido existente : devolucoes) {
            if (existente.getRemessaOriginal() == remessaOriginal) {
                throw new IllegalStateException("Remessa já possui devolução.");
            }
        }
        devolucoes.add(devolucao);
        return devolucao;
    }

    public int contarRemessas() {
        return remessas.size();
    }

    public int contarDevolucoes() {
        return devolucoes.size();
    }

    private void verificarCodigoLivre(String codigo) {
        for (RemessaPedido r : remessas) {
            if (r.getCodigoRastreio().equals(codigo)) {
                throw new IllegalStateException("Código de rastreio repetido: " + codigo);
            }
        }
        for (DevolucaoPedido d : devolucoes) {
            if (d.getCodigoRastreio().equals(codigo)) {
                throw new IllegalStateException("Código de rastreio repetido: " + codigo);
            }
        }
    }
}
