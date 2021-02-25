package br.com.study.services;

public class Orcamento {
    private final String nomeLoja;
    private final double preco;
    private final Desconto.Codigo codigo;

    private Orcamento(String nomeLoja, double preco, Desconto.Codigo codigo) {
        this.nomeLoja = nomeLoja;
        this.preco = preco;
        this.codigo = codigo;
    }

    public String getNomeLoja() {
        return nomeLoja;
    }

    public double getPreco() {
        return preco;
    }

    public Desconto.Codigo getCodigo() {
        return codigo;
    }

    public static Orcamento parse(String s) {
        String[] split = s.split(":");
        String nomeLoja = split[0];
        Double preco = Double.parseDouble(split[1]);
        Desconto.Codigo codigo = Desconto.Codigo.valueOf(split[2]);

        return new Orcamento(nomeLoja, preco, codigo);
    }
}
