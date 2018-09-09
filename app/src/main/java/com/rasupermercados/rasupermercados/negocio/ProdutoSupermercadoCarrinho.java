package com.rasupermercados.rasupermercados.negocio;

import java.util.Date;
import java.util.List;

public class ProdutoSupermercadoCarrinho {
    ProdutoSupermercado produtoSupermercado;
    Date data;

    public void setData(Date data) {
        this.data = data;
    }

    public void setProdutoSupermercado(ProdutoSupermercado produtoSupermercado) {
        this.produtoSupermercado = produtoSupermercado;
    }

    public Date getData() {
        return data;
    }

    public ProdutoSupermercado getProdutoSupermercado() {
        return produtoSupermercado;
    }
}
