package com.rasupermercados.rasupermercados.negocio;

import java.util.ArrayList;
import java.util.List;

public class Carrinho {
    List<ProdutoSupermercadoCarrinho> produtoSupermercadoCarrinhos;
    double valorTotal;

    public Carrinho() {
        this.produtoSupermercadoCarrinhos = new ArrayList<>();
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void addProdutoSupermercadoCarrinho(ProdutoSupermercadoCarrinho produtoSupermercadoCarrinho) {
        this.produtoSupermercadoCarrinhos.add(produtoSupermercadoCarrinho);

        setValorTotal(this.valorTotal + produtoSupermercadoCarrinho.getQuantidade() * produtoSupermercadoCarrinho.getProdutoSupermercado().getValor());

    }

    public void setProdutoSupermercadoCarrinhos(List<ProdutoSupermercadoCarrinho> produtoSupermercadoCarrinhos) {
        this.produtoSupermercadoCarrinhos = produtoSupermercadoCarrinhos;
    }

    public List<ProdutoSupermercadoCarrinho> getProdutoSupermercadoCarrinhos() {
        return produtoSupermercadoCarrinhos;
    }
}
