package br.com.study.execute;

import java.util.concurrent.*;

public class FutureTest {
    private static ExecutorService executorService = Executors.newFixedThreadPool(1);

    public static void main(String[] args) {
        Future<Double> future = executorService.submit(() -> {
            TimeUnit.SECONDS.sleep(2);
            return 2000D;
        });
        enrolando();
        try{
            while (!future.isDone()) {
                Double resultado = future.get();
                System.out.println(resultado);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static void enrolando() {
        long cont = 0;
        for (int i = 0; i < 1_000_000; i++) {
            cont+=i;
        }
        System.out.println(cont);
    }
}
