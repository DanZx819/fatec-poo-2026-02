package a06.lojaonline;

import java.text.Normalizer;

public enum StatusRastreamento {
    PREPARANDO,
    EM_TRANSITO,
    CONCLUIDO;

    public static StatusRastreamento converter(String texto) {
        if (texto == null) {
            throw new IllegalArgumentException("Status não pode ser nulo.");
        }
        String normalizado = Normalizer.normalize(texto.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[\\s-]+", "_")
                .toUpperCase();
        try {
            return StatusRastreamento.valueOf(normalizado);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status desconhecido: " + texto);
        }
    }

    public boolean podeAvancarPara(StatusRastreamento novo) {
        return novo.ordinal() == this.ordinal() + 1;
    }
}
