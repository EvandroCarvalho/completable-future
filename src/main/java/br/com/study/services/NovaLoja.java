package br.com.study.services;

import java.util.List;
import java.util.concurrent.*;

import static java.util.Arrays.asList;

public class NovaLoja {

    private String nome;

    public NovaLoja(String nome) {
        this.nome = nome;
    }

    public NovaLoja() {
    }

    public String getNome() {
        return nome;
    }

    public String getPeco() {
        double preco = calcularPreco();
        Desconto.Codigo codigo = Desconto.Codigo.values()[
                ThreadLocalRandom.current().nextInt(Desconto.Codigo.values().length)];
        return String.format("%s:%.2f:%s", nome, preco, codigo);
    }

    private double calcularPreco() {
        delay();
//        System.out.println(1/0);
        return ThreadLocalRandom.current().nextDouble() * 100;
    }

    private static void delay() {
        try {
            int delay = ThreadLocalRandom.current().nextInt(500, 2000);
            TimeUnit.SECONDS.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static List<NovaLoja> lojas() {
        return asList(
                new NovaLoja("americanas"),
                new NovaLoja("casasBahia"),
                new NovaLoja("bestbuy"),
                new NovaLoja("wallmart"),
                new NovaLoja("Amazon")
        );
    }
}
