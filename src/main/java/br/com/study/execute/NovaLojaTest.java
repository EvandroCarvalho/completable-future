package br.com.study.execute;

import br.com.study.services.Desconto;
import br.com.study.services.NovaLoja;
import br.com.study.services.Orcamento;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NovaLojaTest {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        List<NovaLoja> novaLojas = NovaLoja.lojas();
        novaLojas.stream();
//                .forEach(novaLoja -> System.out.println(novaLoja.getPeco()));
//        acharPrecos(novaLojas);
        Executor executor = Executors.newFixedThreadPool(Math.min(novaLojas.size(), 100), r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
        acharPrecosAsync(novaLojas, executor);

        CompletableFuture[] completableFutures = acharPrecosAsyncStream(novaLojas, executor)
                .map(f -> f.thenAccept(System.out::println))
                .toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(completableFutures).join();

    }

    private static List<String> acharPrecos(List<NovaLoja> lojas) {
        LocalDateTime now = LocalDateTime.now();
        List<String> collect = lojas.stream()
                .map(NovaLoja::getPeco)
                .map(Orcamento::parse)
                .map(Desconto::calcularDesconto)
                .collect(Collectors.toList());
        System.out.println("Tempo total stream: " + Duration.between(now, LocalDateTime.now()).toMillis());
        System.out.println(collect);

        return collect;
    }

    private static List<String> acharPrecosAsync(List<NovaLoja> lojas, Executor executor) {
        System.out.println("Completable Future Async");

        LocalDateTime now = LocalDateTime.now();
        List<CompletableFuture<String>> futureList = lojas.stream()
                // pegando o preço original de forma async
                .map(loja -> CompletableFuture.supplyAsync(loja::getPeco, executor))
                // transforma a string em orçamento no momento em que ele se torna disponivel
                .map(future -> future.thenApply(Orcamento::parse))
                // compoem o primeiro future com mais uma chamada async, para pegar os descontos
                // no momento que a primeira requisicao async estiver disponivel
                .map(future -> future.thenCompose(orcamento ->
                        CompletableFuture.supplyAsync(() -> Desconto.calcularDesconto(orcamento), executor)))
                .collect(Collectors.toList());
        //Espera todos os futures no stram finalizarem para terem os resultados extraidos
        List<String> collect = futureList.stream().map(CompletableFuture::join).collect(Collectors.toList());

        System.out.println("Tempo total stream: " + Duration.between(now, LocalDateTime.now()).toMillis());
        System.out.println(collect);

        return collect;
    }

    private static Stream<CompletableFuture<String>> acharPrecosAsyncStream(List<NovaLoja> lojas, Executor executor) {
        System.out.println("Completable Future Async Stream");

        LocalDateTime now = LocalDateTime.now();
        Stream<CompletableFuture<String>> completableFutureStream = lojas.stream()
                .map(loja -> CompletableFuture.supplyAsync(loja::getPeco, executor))
                .map(future -> future.thenApply(Orcamento::parse))
                .map(future -> future.thenCompose(orcamento ->
                        CompletableFuture.supplyAsync(() -> Desconto.calcularDesconto(orcamento), executor)));

        System.out.println("Tempo total stream: " + Duration.between(now, LocalDateTime.now()).toMillis());

        return completableFutureStream;
    }

}
