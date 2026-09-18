package com.ucsal.visitor;

// Define uma operação para lâmpadas e outra para aparelhos de ar-condicionado

public interface Visitante {
    void visitar(Lampada lampada);
    void visitar(ArCondicionado ar);
}
