package br.com.study.services;

import java.util.concurrent.*;

public class Loja {

    private String nome;

    public Loja(String nome) {
        this.nome = nome;
    }

    public Loja() {
    }

    public String getNome() {
        return nome;
    }

    public double getPeco() {
        return calcularPreco();
    }

    public CompletableFuture<Double> getPrecoAsync() {
        CompletableFuture<Double> precoFuturo = new CompletableFuture<>();
        new Thread(() -> {
            try {
                precoFuturo.complete(calcularPreco());
            } catch (Exception e) {
                precoFuturo.completeExceptionally(e); // lança uma exceçõa controlada
            }

        }).start();

        return precoFuturo;
    }

    // mesma coisa que a classe getPrecoAsync mais mais exuto
    public CompletableFuture<Double> getPrecoAsyncTunado() {
        return CompletableFuture.supplyAsync(this::calcularPreco);
    }

        private double calcularPreco() {
        delay();
        return ThreadLocalRandom.current().nextDouble() * 1000;
    }

    private static void delay() {
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
