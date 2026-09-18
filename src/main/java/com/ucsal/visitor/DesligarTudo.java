package com.ucsal.visitor;

// Simula o desligamento dos dispositivos ao sair de casa, exibindo uma mensagem para cada um
 
public class DesligarTudo implements Visitante {

    @Override
    public void visitar(Lampada lampada) {
        System.out.println("   Apagando a luz do comodo: " + lampada.getComodo());
    }

    @Override
    public void visitar(ArCondicionado ar) {
        System.out.println("   Desligando o ar do comodo: " + ar.getComodo());
    }
}
