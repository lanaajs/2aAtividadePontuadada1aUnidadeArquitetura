package com.ucsal.visitor;

// Representa uma lâmpada, com seu cômodo e potência em watts

public class Lampada implements Dispositivo {

    private final String comodo;
    private final int watts;

    public Lampada(String comodo, int watts) {
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
        visitante.visitar(this); // Chama o método do visitante que recebe uma lâmpada
    }
}
