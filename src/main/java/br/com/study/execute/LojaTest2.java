package br.com.study.execute;

import br.com.study.services.Loja;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class LojaTest2 {
    public static void main(String[] args) {
        List<Loja> lojas = asList(
                new Loja("americanas"),
                new Loja("casasBahia"),
                new Loja("bestbuy"),
                new Loja("wallmart"),
                new Loja("americanas"),
                new Loja("casasBahia"),
                new Loja("bestbuy"),
                new Loja("wallmart"),
                new Loja("americanas"),
                new Loja("casasBahia"),
                new Loja("bestbuy"),
                new Loja("wallmart")
        );
        // NumeroDeThreads = Ncpu * Ucpu * (1+W/C)
        // Ncpu = numero de cores disponíveis
        // Ucpu = quantidade de utilização da utilização da CPU (0-1), 1 = 100%
        // W/C = wait time e compute time
        // Nthreads = 8 * 1 * (1+99) = 800
        System.out.println(Runtime.getRuntime().availableProcessors());
        Executor executor = Executors.newFixedThreadPool(lojas.size(), r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
//        acharPrecos(lojas);
        acharPrecos2(lojas);
//        acharPrecos3(lojas);
        acharPrecos4(lojas);
        acharPrecos5(lojas, executor);
    }

    private static List<String> acharPrecos(List<Loja> lojas) {
        LocalDateTime now = LocalDateTime.now();
        List<String> collect = lojas.stream()
                .map(loja -> String.format("%s o preço é: %.2f", loja.getNome(), loja.getPeco()))
                .collect(Collectors.toList());
        System.out.println("Tempo total stream: " + Duration.between(now, LocalDateTime.now()).toMillis());
        System.out.println(collect);

        return collect;
    }

    private static List<String> acharPrecos2(List<Loja> lojas) {
        LocalDateTime now = LocalDateTime.now();
        List<String> collect = lojas.parallelStream()
                .map(loja -> String.format("%s o preço é: %.2f", loja.getNome(), loja.getPeco()))
                .collect(Collectors.toList());
        System.out.println("Tempo total utilizando parallelStream: " + Duration.between(now, LocalDateTime.now()).toMillis());
        System.out.println(collect);

        return collect;
    }

    private static List<String> acharPrecos3(List<Loja> lojas) {
        LocalDateTime now = LocalDateTime.now();

        List<String> collect = lojas.stream()
                .map(loja -> CompletableFuture.supplyAsync(
                        () -> String.format("%s o preço é: %.2f", loja.getNome(), loja.getPeco())
                ))
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        System.out.println("Tempo total completableFuture sequencial: " + Duration.between(now, LocalDateTime.now()).toMillis());
        System.out.println(collect);

        return collect;
    }

    private static List<String> acharPrecos4(List<Loja> lojas) {
        LocalDateTime now = LocalDateTime.now();

        List<CompletableFuture<String>> precoFuturo = lojas.stream()
                .map(loja -> CompletableFuture.supplyAsync(
                        () -> String.format("%s o preço é: %.2f", loja.getNome(), loja.getPeco())
                ))
                .collect(Collectors.toList());
        System.out.println("Tempo total completableFuture sequencial invocação: " + Duration.between(now, LocalDateTime.now()).toMillis());

        List<String> collect = precoFuturo.stream().map(CompletableFuture::join).collect(Collectors.toList());

        System.out.println("Tempo total completableFuture sequencial invocação: " + Duration.between(now, LocalDateTime.now()).toMillis());
        System.out.println(collect);

        return collect;
    }

    private static List<String> acharPrecos5(List<Loja> lojas, Executor executor) {
        LocalDateTime now = LocalDateTime.now();

        List<CompletableFuture<String>> precoFuturo = lojas.stream()
                .map(loja -> CompletableFuture
                        .supplyAsync(
                                () -> String.format("%s o preço é: %.2f", loja.getNome(), loja.getPeco()), executor
                        )
                )
                .collect(Collectors.toList());
        System.out.println("Tempo total completableFuture sequencial invocação: " + Duration.between(now, LocalDateTime.now()).toMillis());


        List<String> collect = precoFuturo.stream().map(CompletableFuture::join).collect(Collectors.toList());

        System.out.println("Tempo total completableFuture sequencial invocação: " + Duration.between(now, LocalDateTime.now()).toMillis());
        System.out.println(collect);

        return collect;
    }
}
