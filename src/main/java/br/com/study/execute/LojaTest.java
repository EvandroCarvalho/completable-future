package br.com.study.execute;


import br.com.study.services.Loja;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class LojaTest {

    public static void main(String[] args) {
        Loja americanas = new Loja();
        Loja casasBahia = new Loja();
        Loja bestBuy = new Loja();
        Loja wallmart = new Loja();

//        LocalDateTime now = LocalDateTime.now();
//        System.out.println(americanas.getPeco());
//        System.out.println(casasBahia.getPeco());
//        System.out.println(bestBuy.getPeco());
//        System.out.println(wallmart.getPeco());
//        System.out.println(Duration.between(now, LocalDateTime.now()).toMillis());

        LocalDateTime now = LocalDateTime.now();
        Future<Double> americanasPrecoAsync = americanas.getPrecoAsync();
        Future<Double> casasBahiaPrecoAsync = casasBahia.getPrecoAsync();
        Future<Double> bestBuyPrecoAsync = bestBuy.getPrecoAsync();
        Future<Double> wallmartPrecoAsync = wallmart.getPrecoAsync();
        System.out.println("Tempo invocação: " + Duration.between(now, LocalDateTime.now()).toMillis());

        try {
            System.out.println("Americanas " + americanasPrecoAsync.get());
            System.out.println("CasaBahia " + casasBahiaPrecoAsync.get());
            System.out.println("BestBuy " + bestBuyPrecoAsync.get());
            System.out.println("Wallmart " + wallmartPrecoAsync.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("Tempo para pegar o resultado: " + Duration.between(now, LocalDateTime.now()).toMillis());

    }
}
