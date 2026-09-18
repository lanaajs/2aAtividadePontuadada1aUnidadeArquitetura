package com.ucsal.visitor;

//Representa um ar-condicionado, com seu cômodo e potência em watts

public class ArCondicionado implements Dispositivo {

    private final String comodo;
    private final int watts;

    public ArCondicionado(String comodo, int watts) {
        this.comodo = comodo;
        this.watts = watts;
    }

    public String getComodo() {
        return comodo;
    }

    public int getWatts() {
        return watts;
    }

    @Override
    public void aceitar(Visitante visitante) {
        visitante.visitar(this);
    }
}
