package com.rasupermercados.rasupermercados.negocio;

import java.util.Date;
import java.util.List;

public class ProdutoSupermercadoCarrinho {
    Produto produto;
    ProdutoSupermercado produtoSupermercado;
    int quantidade;


    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProdutoSupermercado(ProdutoSupermercado produtoSupermercado) {
        this.produtoSupermercado = produtoSupermercado;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public ProdutoSupermercado getProdutoSupermercado() {
        return produtoSupermercado;
    }
}
