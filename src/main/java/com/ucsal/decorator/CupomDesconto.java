package com.ucsal.decorator;

// Aplica um desconto em reais sobre o valor da cobrança recebida

public class CupomDesconto implements Cobranca {

    private final Cobranca cobranca;
    private final double desconto;

    public CupomDesconto(Cobranca cobranca, double desconto) {
        this.cobranca = cobranca;
        this.desconto = desconto;
    }

    @Override
    public String descricao() {
        return cobranca.descricao() + " - cupom";
    }

    @Override
    public double valor() {
        return cobranca.valor() - desconto;
    }
}
