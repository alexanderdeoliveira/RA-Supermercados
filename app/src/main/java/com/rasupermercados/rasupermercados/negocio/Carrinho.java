package com.rasupermercados.rasupermercados.negocio;

import java.util.List;

public class Carrinho {
    List<ProdutoSupermercado> produtoSupermercados;

    public void setProdutoSupermercados(List<ProdutoSupermercado> produtoSupermercados) {
        this.produtoSupermercados = produtoSupermercados;
    }

    public List<ProdutoSupermercado> getProdutoSupermercados() {
        return produtoSupermercados;
    }
}
