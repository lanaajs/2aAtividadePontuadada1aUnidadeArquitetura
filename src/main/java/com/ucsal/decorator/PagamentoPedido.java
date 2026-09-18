package com.ucsal.decorator;

// Representa o valor dos produtos do pedido, antes de taxas e descontos

public class PagamentoPedido implements Cobranca {

    private final double valorProdutos;

    public PagamentoPedido(double valorProdutos) {
        this.valorProdutos = valorProdutos;
    }

    @Override
    public String descricao() {
        return "Pedido";
    }

    @Override
    public double valor() {
        return valorProdutos;
    }
}
