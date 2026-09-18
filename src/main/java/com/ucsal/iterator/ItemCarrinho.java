package com.ucsal.iterator;

// Guarda o nome e o preço de um produto do carrinho

public class ItemCarrinho {

    private final String produto;
    private final double preco;

    public ItemCarrinho(String produto, double preco) {
        this.produto = produto;
        this.preco = preco;
    }

    public String getProduto() {
        return produto;
    }

    public double getPreco() {
        return preco;
    }
}
