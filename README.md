# 2ª Atividade Pontuada da 1ª Unidade — Arquitetura de Softwares
# Estudantes: Alana de Jesus e Kayke Queiroz

---

**UCSAL — Padrões de Projeto**
Padrões da categoria **Extensão**: Decorator, Iterator e Visitor

## O que são padrões de extensão

São os padrões que permitem **acrescentar comportamento novo a um sistema sem modificar o código que já existe**. Todos aplicam o Princípio Aberto/Fechado (aberto para extensão, fechado para modificação). A diferença está no que cada um estende:

| Padrão | O que estende |
|---|---|
| **Decorator** | As responsabilidades de um objeto, empilhando camadas |
| **Iterator** | A forma de percorrer uma coleção, sem expor a estrutura interna |
| **Visitor** | As operações aplicáveis a uma família de classes |

---

## Como executar

O projeto usa Gradle (wrapper incluído) e não tem dependências externas.

```bash
./gradlew run
```

No Windows:

```powershell
gradlew.bat run
```

Também é possível abrir a pasta no IntelliJ IDEA e executar a classe `com.ucsal.Main`.

## Estrutura

```
src/main/java/com/ucsal/
├── Main.java                  -> executa as três demonstrações
├── decorator/                 -> sistema de pagamentos
│   ├── Cobranca.java
│   ├── PagamentoPedido.java
│   ├── TaxaAntifraude.java
│   └── CupomDesconto.java
├── iterator/                  -> carrinho de compras
│   ├── ItemCarrinho.java
│   └── CarrinhoDeCompras.java
└── visitor/                   -> automação residencial
    ├── Dispositivo.java
    ├── Lampada.java
    ├── ArCondicionado.java
    ├── Visitante.java
    ├── RelatorioConsumo.java
    └── DesligarTudo.java
```

---

## 1. Decorator — sistema de pagamentos

### Conceito

O Decorator acrescenta responsabilidades a um objeto **envolvendo-o em outro objeto da mesma interface**. Como o invólucro tem o mesmo tipo do objeto envolvido, ele pode ser envolvido de novo, e as camadas se empilham.

Isso substitui a herança pela composição. Sem o padrão, seria preciso criar uma subclasse para cada combinação de encargos (`PagamentoComAntifraude`, `PagamentoComAntifraudeECupom`, e assim por diante). Com o padrão, cada encargo é uma classe só, e as combinações são montadas na hora da execução.

### Problema simulado

No checkout de uma loja, sobre o valor do pedido podem incidir uma taxa antifraude e um cupom de desconto — em qualquer combinação, decidida no momento da compra.

### Código

```java
// Interface comum ao pedido e aos decoradores
public interface Cobranca {
    String descricao();
    double valor();
}
```

```java
// O pedido puro, sem encargos
public class PagamentoPedido implements Cobranca {

    private final double valorProdutos;

    public PagamentoPedido(double valorProdutos) {
        this.valorProdutos = valorProdutos;
    }

    public String descricao() { return "Pedido"; }
    public double valor()     { return valorProdutos; }
}
```

```java
// Decorador: guarda a cobrança recebida e soma seu próprio efeito
public class TaxaAntifraude implements Cobranca {

    private final Cobranca cobranca;

    public TaxaAntifraude(Cobranca cobranca) {
        this.cobranca = cobranca;
    }

    public String descricao() {
        return cobranca.descricao() + " + antifraude";
    }

    public double valor() {
        return cobranca.valor() * 1.02;   // delega e acrescenta 2%
    }
}
```

```java
public class CupomDesconto implements Cobranca {

    private final Cobranca cobranca;
    private final double desconto;

    public CupomDesconto(Cobranca cobranca, double desconto) {
        this.cobranca = cobranca;
        this.desconto = desconto;
    }

    public String descricao() { return cobranca.descricao() + " - cupom"; }
    public double valor()     { return cobranca.valor() - desconto; }
}
```

Montando as camadas:

```java
Cobranca pedido = new PagamentoPedido(200.00);
Cobranca comEncargos = new CupomDesconto(new TaxaAntifraude(pedido), 20.00);
```

### Saída

```
Pedido: R$ 200.00
Pedido + antifraude - cupom: R$ 184.00
```

O pedido original continua intacto: `pedido` ainda vale R$ 200,00. O objeto decorado é outro.

### Vantagens e limitações

Adicionar um encargo novo custa apenas uma classe nova, e as camadas podem ser combinadas livremente em tempo de execução. Em compensação, o código gera muitos objetos pequenos e a depuração fica mais difícil, porque a chamada atravessa várias camadas. Vale lembrar que a **ordem importa**: aplicar o cupom antes ou depois da taxa dá resultados diferentes.

Exemplo real no Java: `new BufferedReader(new InputStreamReader(System.in))`.

---

## 2. Iterator — carrinho de compras

### Conceito

O Iterator permite **percorrer os elementos de uma coleção sem expor como ela é construída por dentro**. A posição do percurso sai da coleção e passa a morar em um objeto separado, o iterador.

Com isso, se a estrutura interna mudar (de array para lista ligada, por exemplo), o código que percorre continua exatamente igual. Como o estado do percurso fica no iterador, também é possível ter vários percursos ao mesmo tempo sobre a mesma coleção.

### Problema simulado

O carrinho de uma loja virtual. Por dentro ele usa um array; por fora, quem consome só pede um iterador e percorre.

### Código

```java
public class ItemCarrinho {

    private final String produto;
    private final double preco;

    public ItemCarrinho(String produto, double preco) {
        this.produto = produto;
        this.preco = preco;
    }

    public String getProduto() { return produto; }
    public double getPreco()   { return preco; }
}
```

```java
public class CarrinhoDeCompras implements Iterable<ItemCarrinho> {

    private final ItemCarrinho[] itens = new ItemCarrinho[10];  // detalhe interno
    private int quantidade = 0;

    public void adicionar(ItemCarrinho item) {
        itens[quantidade++] = item;
    }

    @Override
    public Iterator<ItemCarrinho> iterator() {
        return new IteradorCarrinho();
    }

    // Iterador concreto: guarda a posição atual do percurso
    private class IteradorCarrinho implements Iterator<ItemCarrinho> {

        private int posicao = 0;

        public boolean hasNext() {
            return posicao < quantidade;
        }

        public ItemCarrinho next() {
            return itens[posicao++];
        }
    }
}
```

Uso pelo cliente:

```java
for (ItemCarrinho item : carrinho) {   // o for-each chama iterator() sozinho
    System.out.println(item.getProduto());
}
```

### Saída

```
   Teclado    R$ 150.00
   Mouse      R$ 80.00
   Monitor    R$ 900.00
   TOTAL: R$ 1130.00
```

Repare que o laço nunca menciona array nem índice. Ele não sabe — e não precisa saber — como o carrinho guarda os itens.

### Vantagens e limitações

Encapsula a estrutura de dados e dá uma forma uniforme de percorrer qualquer coleção. Basta implementar `Iterable` para o `for-each` do Java funcionar. Por outro lado, é estrutura demais para casos simples, em que um `ArrayList` já resolveria, e o iterador pode ficar inválido se a coleção for alterada durante o percurso.

Exemplo real no Java: todo o Java Collections Framework é construído sobre `Iterable` e `Iterator`.

---

## 3. Visitor — automação residencial

### Conceito

O Visitor permite **criar uma operação nova sobre uma família de classes sem alterar essas classes**. A operação vira um objeto à parte — o visitante — com um método `visitar` para cada tipo de elemento.

O truque que faz isso funcionar é a **dupla dispensa**. O cliente não chama `visitante.visitar(dispositivo)`, porque nesse caso o Java escolheria a sobrecarga pelo tipo declarado da variável. Em vez disso, chama `dispositivo.aceitar(visitante)`, e cada elemento responde com `visitante.visitar(this)`. Dentro de `Lampada`, o `this` é comprovadamente uma `Lampada`, então a sobrecarga certa é escolhida. São duas decisões, uma de cada lado.

### Problema simulado

Uma casa inteligente com lâmpadas e ar-condicionado. O sistema precisa executar operações sobre todos os aparelhos — relatório de consumo, rotina de sair de casa — e essas operações crescem com o tempo.

### Código

```java
public interface Dispositivo {
    void aceitar(Visitante visitante);
}
```

```java
public class Lampada implements Dispositivo {

    private final String comodo;
    private final int watts;

    public Lampada(String comodo, int watts) {
        this.comodo = comodo;
        this.watts = watts;
    }

    public String getComodo() { return comodo; }
    public int getWatts()     { return watts; }

    @Override
    public void aceitar(Visitante visitante) {
        visitante.visitar(this);   // aqui acontece a dupla dispensa
    }
}
```

```java
// Uma sobrecarga para cada tipo de dispositivo
public interface Visitante {
    void visitar(Lampada lampada);
    void visitar(ArCondicionado ar);
}
```

```java
public class RelatorioConsumo implements Visitante {

    private double totalKwh = 0;

    public void visitar(Lampada lampada) {
        double kwh = lampada.getWatts() * 5 * 30 / 1000.0;    // 5 horas por dia
        totalKwh += kwh;
        System.out.printf("   Lampada (%s): %.2f kWh%n", lampada.getComodo(), kwh);
    }

    public void visitar(ArCondicionado ar) {
        double kwh = ar.getWatts() * 8 * 30 / 1000.0;         // 8 horas por dia
        totalKwh += kwh;
        System.out.printf("   Ar-condicionado (%s): %.2f kWh%n", ar.getComodo(), kwh);
    }

    public double getTotalKwh() { return totalKwh; }
}
```

A segunda operação, `DesligarTudo`, foi criada **sem alterar uma linha** de `Lampada` ou `ArCondicionado` — é exatamente o que o padrão promete:

```java
public class DesligarTudo implements Visitante {

    public void visitar(Lampada lampada) {
        System.out.println("   Apagando a luz do comodo: " + lampada.getComodo());
    }

    public void visitar(ArCondicionado ar) {
        System.out.println("   Desligando o ar do comodo: " + ar.getComodo());
    }
}
```

Uso pelo cliente:

```java
List<Dispositivo> casa = List.of(
        new Lampada("Sala", 12),
        new Lampada("Cozinha", 15),
        new ArCondicionado("Quarto", 1400));

RelatorioConsumo relatorio = new RelatorioConsumo();
for (Dispositivo d : casa) {
    d.aceitar(relatorio);
}
```

### Saída

```
Relatorio de consumo:
   Lampada (Sala): 1.80 kWh
   Lampada (Cozinha): 2.25 kWh
   Ar-condicionado (Quarto): 336.00 kWh
   TOTAL: 340.05 kWh
Rotina sair de casa:
   Apagando a luz do comodo: Sala
   Apagando a luz do comodo: Cozinha
   Desligando o ar do comodo: Quarto
```

### Vantagens e limitações

Uma operação nova custa só uma classe nova, e a lógica fica concentrada em vez de espalhada pelas classes de domínio. A contrapartida é forte: **adicionar um tipo novo de dispositivo é caro**. Incluir uma `Persiana` obrigaria a alterar a interface `Visitante` e todos os visitantes já escritos. Por isso o Visitor só compensa quando a família de classes é estável e são as operações que mudam.

Exemplo real no Java: `java.nio.file.FileVisitor`, usado por `Files.walkFileTree`.

---

## Comparação final

| Critério | Decorator | Iterator | Visitor |
|---|---|---|---|
| O que estende | Responsabilidades de um objeto | Formas de percorrer | Operações sobre uma hierarquia |
| Mecanismo | Um objeto envolve outro do mesmo tipo | Estado do percurso em objeto separado | Dupla dispensa (`aceitar` + `visitar`) |
| Adicionar comportamento | Barato | Barato | Barato |
| Adicionar tipo novo | Barato | Barato | **Caro** |
| Exemplo no Java | `BufferedReader` | `java.util.Iterator` | `FileVisitor` |

---

## Referências

- GAMMA, E. et al. **Padrões de Projeto: Soluções Reutilizáveis de Software Orientado a Objetos**. Porto Alegre: Bookman, 2000.
- FREEMAN, E.; ROBSON, E. **Use a Cabeça! Padrões de Projetos**. 2. ed. Rio de Janeiro: Alta Books, 2009.
- Refactoring Guru — Catálogo de Padrões de Projeto: <https://refactoring.guru/pt-br/design-patterns>
