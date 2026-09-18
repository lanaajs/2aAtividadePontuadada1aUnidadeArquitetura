package com.ucsal.visitor;

/**
 * Estima o consumo mensal dos dispositivos e soma o total em kWh
 * Considera 30 dias, com 5 horas diárias para lâmpadas e 8 para o ar-condicionado
 */

public class RelatorioConsumo implements Visitante {

    private double totalKwh = 0;

    @Override
    public void visitar(Lampada lampada) {
        double kwh = lampada.getWatts() * 5 * 30 / 1000.0; // Uso de 5 horas por dia durante 30 dias
        totalKwh += kwh;
        System.out.printf("   Lampada (%s): %.2f kWh%n", lampada.getComodo(), kwh);
    }

    @Override
    public void visitar(ArCondicionado ar) {
        double kwh = ar.getWatts() * 8 * 30 / 1000.0; // 8 horas por dia durante 30 dias
        totalKwh += kwh;
        System.out.printf("   Ar-condicionado (%s): %.2f kWh%n", ar.getComodo(), kwh);
    }

    public double getTotalKwh() {
        return totalKwh;
    }
}
