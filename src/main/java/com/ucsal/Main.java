package com.ucsal;

import com.ucsal.decorator.*;
import com.ucsal.iterator.*;
import com.ucsal.visitor.*;

import java.util.List;

// Demonstração de padrões de projeto: Decorator, Iterator e Visitor
public class Main {

    public static void main(String[] args) {

        // Aplicando o padrão Decorator
        // Exemplo: aplica taxa e desconto ao pedido
        System.out.println("=== DECORATOR - Sistema de Pagamentos ===");

        Cobranca pedido = new PagamentoPedido(200.00);
        System.out.printf("%s: R$ %.2f%n", pedido.descricao(), pedido.valor());

        // Primeiro aplica a taxa antifraude, depois desconta o cupom
        Cobranca comEncargos = new CupomDesconto(new TaxaAntifraude(pedido), 20.00);
        System.out.printf("%s: R$ %.2f%n", comEncargos.descricao(), comEncargos.valor());

        // Aplicando o padrão Iterator
        // Exemplo: percorre os produtos do carrinho
        System.out.println("\n=== ITERATOR - carrinho de compras ===");

        CarrinhoDeCompras carrinho = new CarrinhoDeCompras();
        carrinho.adicionar(new ItemCarrinho("Teclado", 150.00));
        carrinho.adicionar(new ItemCarrinho("Mouse", 80.00));
        carrinho.adicionar(new ItemCarrinho("Monitor", 900.00));

        double total = 0;
        for (ItemCarrinho item : carrinho) { // O for-each usa o iterador do carrinho
            System.out.printf("   %-10s R$ %.2f%n", item.getProduto(), item.getPreco());
            total += item.getPreco();
        }
        
        System.out.printf("   TOTAL: R$ %.2f%n", total);

        // Aplicando o padrão Visitor
        // Exemplo: calcula o consumo e simula o desligamento dos dispositivos
        System.out.println("\n=== VISITOR - automacao residencial ===");

        List<Dispositivo> casa = List.of(
                new Lampada("Sala", 12),
                new Lampada("Cozinha", 15),
                new ArCondicionado("Quarto", 1400));

        System.out.println("Relatorio de consumo:");
        RelatorioConsumo relatorio = new RelatorioConsumo();

        for (Dispositivo d : casa) {
            d.aceitar(relatorio);
        }

        System.out.printf("   TOTAL: %.2f kWh%n", relatorio.getTotalKwh());
        System.out.println("Rotina sair de casa:");

        DesligarTudo desligar = new DesligarTudo();
        for (Dispositivo d : casa) {
            d.aceitar(desligar);
        }
    }
}
