package com.rasupermercados.rasupermercados.negocio;

import java.util.Date;
import java.util.List;

public class ProdutoSupermercadoCarrinho {
    ProdutoSupermercado produtoSupermercado;
    double quantidade;
    Date data;

    public void setData(Date data) {
        this.data = data;
    }

    public void setProdutoSupermercado(ProdutoSupermercado produtoSupermercado) {
        this.produtoSupermercado = produtoSupermercado;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public Date getData() {
        return data;
    }

    public ProdutoSupermercado getProdutoSupermercado() {
        return produtoSupermercado;
    }
}
