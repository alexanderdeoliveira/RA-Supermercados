package com.rasupermercados.rasupermercados.negocio;

public class ProdutoSupermercado {
    Produto produto;
    Supermercado supermercado;
    double valor;

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public void setSupermercado(Supermercado supermercado) {
        this.supermercado = supermercado;
    }

    public double getValor() {
        return valor;
    }

    public Produto getProduto() {
        return produto;
    }

    public Supermercado getSupermercado() {
        return supermercado;
    }
}
