# 2ª Atividade Pontuada da 1ª Unidade — Arquitetura de Softwares

### Estudantes

**Alana de Jesus e Kayke Queiroz**

### UCSAL — Padrões de Projeto

Este projeto apresenta a implementação e demonstração de três padrões de projeto da categoria **Extensão**:

* **Decorator**
* **Iterator**
* **Visitor**

O objetivo é demonstrar, por meio de exemplos práticos em Java, como esses padrões permitem ampliar o comportamento de um sistema mantendo o código organizado, reutilizável e de fácil manutenção.

---

## 📚 O que são padrões de extensão?

Os padrões de extensão são utilizados quando precisamos **adicionar novos comportamentos ou funcionalidades a um sistema sem modificar diretamente o código que já existe**.

Eles estão relacionados ao **Princípio Aberto/Fechado (Open/Closed Principle)**, segundo o qual uma estrutura deve estar:

> **Aberta para extensão, mas fechada para modificação.**

Cada padrão apresentado neste projeto realiza essa extensão de uma maneira diferente:

| Padrão        | O que estende                                                      |
| ------------- | ------------------------------------------------------------------ |
| **Decorator** | As responsabilidades de um objeto, utilizando camadas              |
| **Iterator**  | A forma de percorrer uma coleção sem expor sua estrutura interna   |
| **Visitor**   | As operações que podem ser realizadas sobre uma família de classes |

---

# 🚀 Como executar

O projeto utiliza **Gradle**, com o wrapper já incluído, e não possui dependências externas.

### Linux/macOS

```bash
./gradlew run
```

### Windows

```powershell
gradlew.bat run
```

Também é possível abrir a pasta do projeto utilizando o **IntelliJ IDEA** e executar diretamente a classe:

```text
com.ucsal.Main
```

---

# 📂 Estrutura do projeto

```text
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

# 1️⃣ Decorator — Sistema de pagamentos

## 📌 Conceito

O padrão **Decorator** permite adicionar novas responsabilidades a um objeto sem modificar sua classe original.

Para isso, o objeto original é envolvido por outro objeto que implementa a mesma interface e adiciona um novo comportamento.

Essa abordagem utiliza **composição em vez de herança**.

Sem o Decorator, seria necessário criar várias subclasses para representar todas as combinações possíveis, como:

```text
PagamentoComAntifraude
PagamentoComCupom
PagamentoComAntifraudeECupom
```

Com o padrão, cada comportamento é implementado separadamente e as combinações são realizadas durante a execução do programa.

---

## 🛒 Problema simulado

Imagine o checkout de uma loja virtual.

Inicialmente existe apenas o valor do pedido, mas durante a compra podem ser aplicados:

* uma **taxa antifraude**;
* um **cupom de desconto**;
* ou os dois ao mesmo tempo.

O Decorator permite realizar essas combinações sem alterar a classe responsável pelo pagamento original.

---

## 💻 Código

### Interface comum ao pedido e aos decoradores

```java
// Interface comum ao pedido e aos decoradores
public interface Cobranca {
    String descricao();
    double valor();
}
```

### Pedido original

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

### Taxa antifraude

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

### Cupom de desconto

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

### Montando as camadas

```java
Cobranca pedido = new PagamentoPedido(200.00);
Cobranca comEncargos = new CupomDesconto(new TaxaAntifraude(pedido), 20.00);
```

---

## 🖥️ Saída

```text
Pedido: R$ 200.00
Pedido + antifraude - cupom: R$ 184.00
```

### Entendendo o resultado

O pedido começa com:

```text
R$ 200,00
```

A taxa antifraude adiciona **2%**:

```text
R$ 200,00 × 1,02 = R$ 204,00
```

Depois, o cupom aplica um desconto de **R$ 20,00**:

```text
R$ 204,00 - R$ 20,00 = R$ 184,00
```

O objeto original continua representando um pedido de **R$ 200,00**. As alterações são realizadas pelas camadas adicionadas ao objeto.

---

## ✅ Vantagens e limitações

### Vantagens

* Permite adicionar novos comportamentos sem alterar classes existentes.
* Evita a criação de muitas subclasses.
* Permite combinar funcionalidades durante a execução.
* Favorece a composição de objetos.

### Limitações

* Pode gerar muitos objetos pequenos.
* Muitas camadas podem dificultar a depuração.
* A ordem dos decoradores pode alterar o resultado final.

### Exemplo no Java

```java
new BufferedReader(new InputStreamReader(System.in))
```

---

# 2️⃣ Iterator — Carrinho de compras

## 📌 Conceito

O padrão **Iterator** permite percorrer os elementos de uma coleção **sem expor como essa coleção é armazenada internamente**.

A responsabilidade de controlar a posição atual do percurso é transferida para um objeto separado: o **iterador**.

Isso significa que a estrutura interna pode mudar sem obrigar o código que percorre os elementos a mudar também.

Por exemplo, o armazenamento poderia mudar de um array para outra estrutura e o código externo poderia continuar utilizando a mesma forma de percurso.

---

## 🛒 Problema simulado

Neste exemplo, temos o carrinho de compras de uma loja virtual.

Internamente, os produtos são armazenados em um **array**.

Porém, quem utiliza o carrinho não precisa conhecer esse detalhe. O código solicita um iterador e percorre normalmente os produtos.

---

## 💻 Código

### Item do carrinho

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

### Carrinho de compras

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

### Utilização

```java
for (ItemCarrinho item : carrinho) {   // o for-each chama iterator() sozinho
    System.out.println(item.getProduto());
}
```

---

## 🖥️ Saída

```text
Teclado    R$ 150.00
Mouse      R$ 80.00
Monitor    R$ 900.00
TOTAL: R$ 1130.00
```

### Entendendo o resultado

O ponto principal é que o `for-each` não precisa saber que os itens estão armazenados em um array.

O código simplesmente percorre:

```java
for (ItemCarrinho item : carrinho)
```

Ou seja, quem utiliza o carrinho **não precisa conhecer sua implementação interna**.

---

## ✅ Vantagens e limitações

### Vantagens

* Encapsula a estrutura interna da coleção.
* Oferece uma maneira uniforme de percorrer elementos.
* Permite diferentes percursos sobre uma mesma coleção.
* Integra-se naturalmente ao `for-each` do Java através de `Iterable`.

### Limitações

* Pode ser desnecessário em coleções muito simples.
* Alterações na coleção durante o percurso podem causar problemas.
* Em alguns casos, estruturas prontas como `ArrayList` já resolvem a necessidade.

### Exemplo no Java

O próprio **Java Collections Framework** utiliza amplamente:

```java
Iterable
Iterator
```

---

# 3️⃣ Visitor — Automação residencial

## 📌 Conceito

O padrão **Visitor** permite criar novas operações sobre uma família de classes **sem precisar alterar diretamente essas classes**.

A operação é colocada em um objeto separado chamado **visitante**.

Esse visitante possui métodos específicos para trabalhar com cada tipo de elemento existente no sistema.

Um dos principais conceitos utilizados pelo Visitor é a chamada **dupla dispensa**.

Em vez de o cliente chamar diretamente:

```text
visitante.visitar(dispositivo)
```

o dispositivo recebe o visitante:

```text
dispositivo.aceitar(visitante)
```

Depois, o próprio dispositivo direciona a chamada para o método correspondente ao seu tipo.

---

## 🏠 Problema simulado

Imagine um sistema de automação residencial com:

* lâmpadas;
* aparelhos de ar-condicionado.

O sistema precisa realizar diferentes operações sobre esses dispositivos, como:

* gerar um relatório de consumo de energia;
* executar uma rotina para desligar todos os aparelhos ao sair de casa.

Novas operações podem surgir com o tempo.

Com o Visitor, essas operações podem ser adicionadas sem alterar diretamente as classes `Lampada` e `ArCondicionado`.

---

## 💻 Código

### Interface dos dispositivos

```java
public interface Dispositivo {
    void aceitar(Visitante visitante);
}
```

### Lâmpada

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

### Interface Visitante

```java
// Uma sobrecarga para cada tipo de dispositivo
public interface Visitante {
    void visitar(Lampada lampada);
    void visitar(ArCondicionado ar);
}
```

### Relatório de consumo

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

### Desligar todos os dispositivos

A operação `DesligarTudo` pode ser adicionada sem alterar as classes dos dispositivos.

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

### Utilização

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

---

## 🖥️ Saída

```text
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

---

## 🔄 Como funciona o Visitor?

O processo pode ser resumido da seguinte maneira:

```text
Cliente
   │
   ▼
Dispositivo
   │
   │ aceitar(visitante)
   ▼
Lampada / ArCondicionado
   │
   │ visitante.visitar(this)
   ▼
Visitante
   │
   ├── visitar(Lampada)
   │
   └── visitar(ArCondicionado)
```

Assim, cada dispositivo direciona o visitante para o método adequado ao seu tipo.

---

## ✅ Vantagens e limitações

### Vantagens

* Facilita a criação de novas operações.
* Evita espalhar a lógica pelas classes de domínio.
* Mantém operações relacionadas agrupadas.
* Permite adicionar novos visitantes sem alterar os dispositivos existentes.

### Limitações

A principal limitação aparece quando precisamos adicionar **um novo tipo de dispositivo**.

Por exemplo, caso seja adicionada uma classe:

```text
Persiana
```

seria necessário adicionar um novo método na interface `Visitante` e atualizar os visitantes existentes.

Por isso, o Visitor é mais indicado quando **os tipos de objetos são relativamente estáveis, mas as operações realizadas sobre eles mudam com frequência**.

### Exemplo no Java

```java
java.nio.file.FileVisitor
```

Utilizado, por exemplo, pelo método:

```java
Files.walkFileTree
```

---

# 📊 Comparação final

| Critério                    | Decorator                             | Iterator                              | Visitor                                |
| --------------------------- | ------------------------------------- | ------------------------------------- | -------------------------------------- |
| **O que estende**           | Responsabilidades de um objeto        | Formas de percorrer uma coleção       | Operações sobre uma hierarquia         |
| **Mecanismo principal**     | Um objeto envolve outro do mesmo tipo | Estado do percurso em objeto separado | Dupla dispensa (`aceitar` + `visitar`) |
| **Adicionar comportamento** | Barato                                | Barato                                | Barato                                 |
| **Adicionar novo tipo**     | Barato                                | Barato                                | Caro                                   |
| **Exemplo no Java**         | `BufferedReader`                      | `java.util.Iterator`                  | `FileVisitor`                          |

---

# 🎯 Conclusão

Os três padrões apresentados permitem estender um sistema, porém atuam em situações diferentes.

O **Decorator** é adequado quando precisamos adicionar responsabilidades ou comportamentos a objetos de maneira dinâmica, sem alterar a classe original.

O **Iterator** resolve o problema de percorrer coleções sem expor sua estrutura interna, separando a lógica de navegação da própria coleção.

Já o **Visitor** facilita a criação de novas operações sobre uma família de objetos, mantendo essas operações separadas das classes que representam os elementos do sistema.

Dessa forma, **Decorator, Iterator e Visitor** demonstram diferentes estratégias para criar sistemas mais flexíveis, organizados e preparados para receber novas funcionalidades.

---

# 📖 Referências

* GAMMA, E. et al. **Padrões de Projeto: Soluções Reutilizáveis de Software Orientado a Objetos**. Porto Alegre: Bookman, 2000.
* FREEMAN, E.; ROBSON, E. **Use a Cabeça! Padrões de Projetos**. 2. ed. Rio de Janeiro: Alta Books, 2009.
* Refactoring Guru — Catálogo de Padrões de Projeto.
