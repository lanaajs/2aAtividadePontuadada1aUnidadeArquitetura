package com.ucsal.decorator;

// Adiciona uma taxa antifraude de 2% ao valor da cobrança recebida
 
public class TaxaAntifraude implements Cobranca {

    private final Cobranca cobranca; // Cobrança usada como base para calcular a taxa

    public TaxaAntifraude(Cobranca cobranca) {
        this.cobranca = cobranca;
    }

    @Override
    public String descricao() {
        return cobranca.descricao() + " + antifraude";
    }

    @Override
    public double valor() {
        return cobranca.valor() * 1.02;
    }
}
