package a06.lojaonline;

import java.util.ArrayList;
import java.util.Locale;

public class LojaMain {

    private static final Locale BR = new Locale("pt", "BR");
    private static int aprovados = 0;
    private static int reprovados = 0;

    private interface Acao {
        void executar();
    }

    public static void main(String[] args) {
        Categoria perifericos = new Categoria(1, "Periféricos");
        Categoria papelaria = new Categoria(2, "Papelaria");
        Catalogo catalogo = new Catalogo();
        catalogo.cadastrarCategoria(perifericos);
        catalogo.cadastrarCategoria(papelaria);

        catalogo.cadastrarProduto(new Produto(101, "Teclado", 120.0, perifericos));
        catalogo.cadastrarProduto(new Produto(102, "Mouse", 50.0, perifericos));
        catalogo.cadastrarProduto(new Produto(103, "Caderno", 30.0, papelaria));
        catalogo.cadastrarProduto(new Produto(104, "Produto teste", 199.99, papelaria));

        Produto teclado = catalogo.buscarProduto(101);
        Produto mouse = catalogo.buscarProduto(102);
        Produto produtoTeste = catalogo.buscarProduto(104);

        ModalidadeEntrega[] modalidades = {
            new EntregaEconomica(),
            new EntregaExpressa(),
            new RetiradaNaLoja()
        };
        ModalidadeEntrega economica = modalidades[0];

        System.out.println("=== Demonstração ===");
        Pedido pedido = new Pedido();
        pedido.adicionarProduto(teclado);
        pedido.adicionarProduto(mouse);
        pedido.adicionarProduto(mouse);
        System.out.println("Linhas: " + pedido.contarItens());
        System.out.println("Subtotal: " + moeda(pedido.calcularSubtotal()));
        for (ModalidadeEntrega modalidade : modalidades) {
            System.out.println(modalidade.getNome() + ": " + moeda(pedido.calcularTotal(modalidade)));
        }

        System.out.println();
        System.out.println("=== Etapa 1 — cenários ===");

        Pedido p1 = new Pedido();
        p1.adicionarProduto(teclado);
        p1.adicionarProduto(mouse, 2);
        verificar("Teclado + 2 mouses: linhas", "2", String.valueOf(p1.contarItens()));
        verificar("Teclado + 2 mouses: subtotal", "R$ 220,00", moeda(p1.calcularSubtotal()));
        verificar("Teclado + 2 mouses: totais econ./expr./retirada",
                "R$ 220,00 / R$ 240,00 / R$ 220,00",
                moeda(p1.calcularTotal(modalidades[0])) + " / "
                + moeda(p1.calcularTotal(modalidades[1])) + " / "
                + moeda(p1.calcularTotal(modalidades[2])));

        ItemPedido itemMouse = new ItemPedido(mouse, 2);
        verificar("ItemPedido.calcularSubtotal: mouse x2", "R$ 100,00", moeda(itemMouse.calcularSubtotal()));

        Pedido p2 = new Pedido();
        p2.adicionarProduto(mouse);
        verificar("1 mouse: subtotal", "R$ 50,00", moeda(p2.calcularSubtotal()));
        verificar("1 mouse: total econômica", "R$ 60,00", moeda(p2.calcularTotal(economica)));

        Pedido p3 = new Pedido();
        p3.adicionarProduto(mouse, 4);
        verificar("4 mouses: subtotal", "R$ 200,00", moeda(p3.calcularSubtotal()));
        verificar("4 mouses: total econômica (frete grátis)", "R$ 200,00", moeda(p3.calcularTotal(economica)));

        Pedido p4 = new Pedido();
        p4.adicionarProduto(produtoTeste);
        verificar("Subtotal 199,99: frete econômico", "R$ 10,00",
                moeda(economica.calcularFrete(p4.calcularSubtotal())));
        verificar("Subtotal 199,99: total econômica", "R$ 209,99", moeda(p4.calcularTotal(economica)));

        Pedido p5 = new Pedido();
        p5.adicionarProduto(mouse);
        p5.adicionarProduto(mouse, 2);
        verificar("Mouse sem qtd + qtd 2: linhas / subtotal", "1 / R$ 150,00",
                p5.contarItens() + " / " + moeda(p5.calcularSubtotal()));

        Pedido p6 = new Pedido();
        p6.adicionarProduto(mouse);
        verificar("Adicionar quantidade 0", "IllegalArgumentException",
                capturar(() -> p6.adicionarProduto(mouse, 0)));
        verificar("Adicionar quantidade -3", "IllegalArgumentException",
                capturar(() -> p6.adicionarProduto(mouse, -3)));
        verificar("Após quantidade inválida: linhas / subtotal preservados", "1 / R$ 50,00",
                p6.contarItens() + " / " + moeda(p6.calcularSubtotal()));

        verificar("Adicionar produto nulo", "IllegalArgumentException",
                capturar(() -> p6.adicionarProduto(null)));
        verificar("Após produto nulo: pedido preservado", "1 / R$ 50,00",
                p6.contarItens() + " / " + moeda(p6.calcularSubtotal()));

        Pedido vazio = new Pedido();
        verificar("Pedido vazio: subtotal / linhas", "R$ 0,00 / 0",
                moeda(vazio.calcularSubtotal()) + " / " + vazio.contarItens());
        verificar("Pedido vazio: calcularTotal", "IllegalStateException",
                capturar(() -> vazio.calcularTotal(economica)));

        verificar("Pedido não vazio com modalidade nula", "IllegalArgumentException",
                capturar(() -> p6.calcularTotal(null)));

        verificar("Filtrar Periféricos", "[Teclado, Mouse]", nomes(catalogo.listarPorCategoria(perifericos)));

        Categoria livros = new Categoria(3, "Livros");
        catalogo.cadastrarCategoria(livros);
        verificar("Filtrar Livros (sem produtos)", "[]", nomes(catalogo.listarPorCategoria(livros)));

        ArrayList<Produto> filtrado = catalogo.listarPorCategoria(perifericos);
        filtrado.clear();
        verificar("Alterar lista filtrada não altera o catálogo", "[Teclado, Mouse]",
                nomes(catalogo.listarPorCategoria(perifericos)));

        verificar("Filtrar categoria nula", "IllegalArgumentException",
                capturar(() -> catalogo.listarPorCategoria(null)));

        ItemPedido item = new ItemPedido(mouse, 2);
        verificar("aumentarQuantidade(0)", "IllegalArgumentException",
                capturar(() -> item.aumentarQuantidade(0)));
        verificar("aumentarQuantidade(-1)", "IllegalArgumentException",
                capturar(() -> item.aumentarQuantidade(-1)));
        verificar("Após incremento inválido: quantidade preservada", "2", String.valueOf(item.getQuantidade()));
        item.aumentarQuantidade(3);
        verificar("aumentarQuantidade(3) soma à quantidade", "5", String.valueOf(item.getQuantidade()));

        verificar("RN01 categoria com código 0", "IllegalArgumentException",
                capturar(() -> new Categoria(0, "X")));
        verificar("RN01 produto com nome em branco", "IllegalArgumentException",
                capturar(() -> new Produto(200, "   ", 10.0, perifericos)));
        verificar("RN02 produto com preço negativo", "IllegalArgumentException",
                capturar(() -> new Produto(200, "X", -1.0, perifericos)));
        verificar("RN02 produto com preço infinito", "IllegalArgumentException",
                capturar(() -> new Produto(200, "X", Double.POSITIVE_INFINITY, perifericos)));
        verificar("RN03 código de produto repetido", "IllegalArgumentException",
                capturar(() -> catalogo.cadastrarProduto(new Produto(101, "Outro", 10.0, perifericos))));
        verificar("RN04 produto com categoria não cadastrada", "IllegalArgumentException",
                capturar(() -> catalogo.cadastrarProduto(new Produto(300, "X", 10.0, new Categoria(9, "Nova")))));

        System.out.println();
        System.out.println("=== Etapa 2 — demonstração da interface ===");

        Pedido pedidoEnvio = new Pedido();
        pedidoEnvio.adicionarProduto(teclado);
        pedidoEnvio.adicionarProduto(mouse, 2);
        double subtotalAntes = pedidoEnvio.calcularSubtotal();

        RegistroRastreamento registro = new RegistroRastreamento();
        RemessaPedido remessa = registro.criarRemessa("ENV-001", pedidoEnvio, new EntregaEconomica());
        remessa.atualizarStatus("em trânsito");
        remessa.atualizarStatus("concluido");

        DevolucaoPedido devolucao =
                registro.criarDevolucao("DEV-001", remessa, "Produto diferente do solicitado");
        devolucao.atualizarStatus("em trânsito");

        Rastreavel[] transportes = { remessa, devolucao };
        for (Rastreavel transporte : transportes) {
            System.out.println(transporte.getCodigoRastreio() + ": " + transporte.consultarRastreamento());
        }

        System.out.println();
        System.out.println("=== Etapa 2 — cenários ===");

        RegistroRastreamento reg = new RegistroRastreamento();
        Pedido pa = new Pedido();
        pa.adicionarProduto(mouse);
        RemessaPedido r = reg.criarRemessa("ENV-100", pa, new EntregaExpressa());
        verificar("Criar remessa válida: status / descrição", "PREPARANDO / Remessa em preparação",
                r.getStatus() + " / " + r.consultarRastreamento());

        r.atualizarStatus("em trânsito");
        verificar("Remessa em trânsito: status / descrição", "EM_TRANSITO / Remessa em trânsito",
                r.getStatus() + " / " + r.consultarRastreamento());

        verificar("Criar devolução de remessa não concluída", "IllegalStateException",
                capturar(() -> reg.criarDevolucao("DEV-X", r, "Defeito")));
        verificar("Após falha: devoluções registradas", "0", String.valueOf(reg.contarDevolucoes()));

        r.atualizarStatus("concluido");
        verificar("Remessa concluída: status / descrição", "CONCLUIDO / Remessa entregue ao cliente",
                r.getStatus() + " / " + r.consultarRastreamento());

        DevolucaoPedido d = reg.criarDevolucao("DEV-100", r, "  Defeito  ");
        verificar("Criar devolução: status / descrição", "PREPARANDO / Devolução em preparação",
                d.getStatus() + " / " + d.consultarRastreamento());
        verificar("Motivo armazenado sem espaços", "Defeito", d.getMotivo());
        verificar("Devolução identifica o pedido pela remessa", "true",
                String.valueOf(d.getRemessaOriginal().getPedido() == pa));

        d.atualizarStatus("em trânsito");
        verificar("Devolução em trânsito: status / descrição", "EM_TRANSITO / Devolução em trânsito",
                d.getStatus() + " / " + d.consultarRastreamento());
        d.atualizarStatus("concluido");
        verificar("Devolução concluída: status / descrição / status da remessa",
                "CONCLUIDO / Devolução recebida pela loja / CONCLUIDO",
                d.getStatus() + " / " + d.consultarRastreamento() + " / " + r.getStatus());

        Rastreavel[] consulta = { r, d };
        StringBuilder sb = new StringBuilder();
        for (Rastreavel t : consulta) {
            sb.append(t.getCodigoRastreio()).append(": ").append(t.consultarRastreamento()).append("; ");
        }
        verificar("Array de Rastreavel", "ENV-100: Remessa entregue ao cliente; DEV-100: Devolução recebida pela loja; ",
                sb.toString());

        int remessasAntes = reg.contarRemessas();
        Pedido pvazio = new Pedido();
        Pedido pb = new Pedido();
        pb.adicionarProduto(teclado);
        verificar("Remessa com pedido nulo", "IllegalArgumentException",
                capturar(() -> reg.criarRemessa("ENV-200", null, new EntregaExpressa())));
        verificar("Remessa com pedido vazio", "IllegalArgumentException",
                capturar(() -> reg.criarRemessa("ENV-201", pvazio, new EntregaExpressa())));
        verificar("Remessa com código nulo", "IllegalArgumentException",
                capturar(() -> reg.criarRemessa(null, pb, new EntregaExpressa())));
        verificar("Remessa com código em branco", "IllegalArgumentException",
                capturar(() -> reg.criarRemessa("   ", pb, new EntregaExpressa())));
        verificar("Remessa com modalidade nula", "IllegalArgumentException",
                capturar(() -> reg.criarRemessa("ENV-202", pb, null)));
        verificar("Remessa com RetiradaNaLoja", "IllegalStateException",
                capturar(() -> reg.criarRemessa("ENV-203", pb, new RetiradaNaLoja())));
        verificar("Remessa com código repetido (ENV-100)", "IllegalStateException",
                capturar(() -> reg.criarRemessa("ENV-100", pb, new EntregaExpressa())));
        verificar("Remessa com código de devolução (DEV-100)", "IllegalStateException",
                capturar(() -> reg.criarRemessa("DEV-100", pb, new EntregaExpressa())));
        verificar("Segunda remessa do mesmo pedido", "IllegalStateException",
                capturar(() -> reg.criarRemessa("ENV-204", pa, new EntregaExpressa())));
        verificar("Após falhas: remessas registradas preservadas", String.valueOf(remessasAntes),
                String.valueOf(reg.contarRemessas()));

        int devolucoesAntes = reg.contarDevolucoes();
        RemessaPedido externa = new RemessaPedido("EXT-1", pb);
        externa.atualizarStatus("em trânsito");
        externa.atualizarStatus("concluido");
        verificar("Devolução com remessa nula", "IllegalArgumentException",
                capturar(() -> reg.criarDevolucao("DEV-200", null, "Defeito")));
        verificar("Devolução com remessa externa ao registro", "IllegalArgumentException",
                capturar(() -> reg.criarDevolucao("DEV-201", externa, "Defeito")));
        verificar("Devolução com motivo nulo", "IllegalArgumentException",
                capturar(() -> reg.criarDevolucao("DEV-202", r, null)));
        verificar("Devolução com motivo em branco", "IllegalArgumentException",
                capturar(() -> reg.criarDevolucao("DEV-203", r, "  ")));
        verificar("Segunda devolução da mesma remessa", "IllegalStateException",
                capturar(() -> reg.criarDevolucao("DEV-204", r, "Outro motivo")));
        verificar("Após falhas: devoluções registradas preservadas", String.valueOf(devolucoesAntes),
                String.valueOf(reg.contarDevolucoes()));

        Pedido pc = new Pedido();
        pc.adicionarProduto(mouse);
        RemessaPedido rc = reg.criarRemessa("ENV-300", pc, new EntregaEconomica());
        verificar("Pular PREPARANDO -> CONCLUIDO", "IllegalStateException",
                capturar(() -> rc.atualizarStatus("concluido")));
        verificar("Repetir PREPARANDO", "IllegalStateException",
                capturar(() -> rc.atualizarStatus("preparando")));
        verificar("Status nulo", "IllegalArgumentException",
                capturar(() -> rc.atualizarStatus(null)));
        verificar("Após transições inválidas: status preservado", "PREPARANDO", String.valueOf(rc.getStatus()));
        rc.atualizarStatus("em trânsito");
        verificar("Retroceder EM_TRANSITO -> PREPARANDO", "IllegalStateException",
                capturar(() -> rc.atualizarStatus("preparando")));
        verificar("Após retroceder: status preservado", "EM_TRANSITO", String.valueOf(rc.getStatus()));

        Pedido pd = new Pedido();
        pd.adicionarProduto(teclado);
        RemessaPedido rd = reg.criarRemessa("  ENV-400  ", pd, new EntregaExpressa());
        verificar("Código com espaços é normalizado", "ENV-400", rd.getCodigoRastreio());
        Pedido pe = new Pedido();
        pe.adicionarProduto(mouse);
        verificar("Repetir código normalizado (\"ENV-400\")", "IllegalStateException",
                capturar(() -> reg.criarRemessa("ENV-400", pe, new EntregaExpressa())));
        verificar("Maiúsculas/minúsculas distintas (\"env-400\") é aceito", "env-400",
                reg.criarRemessa("env-400", pe, new EntregaExpressa()).getCodigoRastreio());

        for (int i = 0; i < 3; i++) {
            for (Rastreavel t : transportes) {
                t.consultarRastreamento();
                t.getStatus();
            }
        }
        devolucao.atualizarStatus("concluido");
        verificar("Após consultas e devolução concluída: subtotal / status da remessa",
                moeda(subtotalAntes) + " / CONCLUIDO",
                moeda(pedidoEnvio.calcularSubtotal()) + " / " + remessa.getStatus());

        System.out.println();
        System.out.println("Cenários aprovados: " + aprovados + " | reprovados: " + reprovados);
    }

    private static void verificar(String cenario, String esperado, String obtido) {
        boolean ok = esperado.equals(obtido);
        if (ok) {
            aprovados++;
        } else {
            reprovados++;
        }
        System.out.println((ok ? "[OK]    " : "[FALHA] ") + cenario
                + " | esperado: " + esperado + " | obtido: " + obtido);
    }

    private static String capturar(Acao acao) {
        try {
            acao.executar();
            return "sem exceção";
        } catch (IllegalArgumentException | IllegalStateException e) {
            return e.getClass().getSimpleName();
        }
    }

    private static String moeda(double valor) {
        return String.format(BR, "R$ %.2f", valor);
    }

    private static String nomes(ArrayList<Produto> produtos) {
        ArrayList<String> lista = new ArrayList<>();
        for (Produto p : produtos) {
            lista.add(p.getNome());
        }
        return lista.toString();
    }
}
