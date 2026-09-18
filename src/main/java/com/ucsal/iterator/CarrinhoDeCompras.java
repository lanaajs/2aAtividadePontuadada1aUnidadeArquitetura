package com.ucsal.iterator;

import java.util.Iterator;

// Guarda até 10 itens e permite percorrê-los com um for-each

public class CarrinhoDeCompras implements Iterable<ItemCarrinho> {

    private final ItemCarrinho[] itens = new ItemCarrinho[10];
    private int quantidade = 0;

    public void adicionar(ItemCarrinho item) {
        itens[quantidade++] = item;
    }

    @Override
    public Iterator<ItemCarrinho> iterator() {
        return new IteradorCarrinho();
    }

    /** Controla a posição para percorrer os itens adicionados ao carrinho. */
    private class IteradorCarrinho implements Iterator<ItemCarrinho> {

        private int posicao = 0;

        @Override
        public boolean hasNext() {
            return posicao < quantidade;
        }

        @Override
        public ItemCarrinho next() {
            return itens[posicao++];
        }
    }
}
