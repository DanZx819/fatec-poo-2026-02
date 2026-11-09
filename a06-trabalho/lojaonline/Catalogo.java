package a06.lojaonline;

import java.util.ArrayList;

public class Catalogo {

    private ArrayList<Categoria> categorias = new ArrayList<>();
    private ArrayList<Produto> produtos = new ArrayList<>();

    public Catalogo() {
    }

    public void cadastrarCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("Categoria não pode ser nula.");
        }
        if (buscarCategoria(categoria.getCodigo()) != null) {
            throw new IllegalArgumentException("Código de categoria já cadastrado.");
        }
        categorias.add(categoria);
    }

    public void cadastrarProduto(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo.");
        }
        if (buscarProduto(produto.getCodigo()) != null) {
            throw new IllegalArgumentException("Código de produto já cadastrado.");
        }
        Categoria registrada = buscarCategoria(produto.getCategoria().getCodigo());
        if (registrada != produto.getCategoria()) {
            throw new IllegalArgumentException("Categoria do produto não está cadastrada no catálogo.");
        }
        produtos.add(produto);
    }

    public Categoria buscarCategoria(int codigo) {
        for (Categoria categoria : categorias) {
            if (categoria.getCodigo() == codigo) {
                return categoria;
            }
        }
        return null;
    }

    public Produto buscarProduto(int codigo) {
        for (Produto produto : produtos) {
            if (produto.getCodigo() == codigo) {
                return produto;
            }
        }
        return null;
    }

    public ArrayList<Produto> listarPorCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("Categoria não pode ser nula.");
        }
        ArrayList<Produto> resultado = new ArrayList<>();
        for (Produto produto : produtos) {
            if (produto.getCategoria().getCodigo() == categoria.getCodigo()) {
                resultado.add(produto);
            }
        }
        return resultado;
    }
}
