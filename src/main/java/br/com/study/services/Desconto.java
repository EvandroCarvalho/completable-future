package br.com.study.services;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Desconto {
    public enum Codigo {
        NENHUM(0), SILVER(5), GOLD(10), PLATINUM(15), ELITE(20);
        private final int procentagem;

        public int getProcentagem() {
            return procentagem;
        }

        Codigo(int procentagem) {
            this.procentagem = procentagem;
        }
    }

    public static String calcularDesconto(Orcamento orcamento) {
        return String.format("%s o preço original: %.2f, desconto:%d, preço final: %.2f", orcamento.getNomeLoja(),
                orcamento.getPreco(), orcamento.getCodigo().getProcentagem(),
                calculo(orcamento.getPreco(), orcamento.getCodigo()));
    }

    public static double calculo(double preco, Codigo codigo) {
        delay();
        return preco * (100 - codigo.procentagem) / 100;
    }

    private static void delay() {
        try {
            int delay = ThreadLocalRandom.current().nextInt(500, 2000);
            TimeUnit.SECONDS.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
