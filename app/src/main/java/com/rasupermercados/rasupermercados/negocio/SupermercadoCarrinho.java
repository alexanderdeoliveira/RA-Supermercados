package com.rasupermercados.rasupermercados.negocio;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SupermercadoCarrinho {
    int qtdProdutos;
    Supermercado supermercado;

    public void setQtdProdutos(int qtdProdutos) {
        this.qtdProdutos = qtdProdutos;
    }

    public void setSupermercado(Supermercado supermercado) {
        this.supermercado = supermercado;
    }

    public int getQtdProdutos() {
        return qtdProdutos;
    }

    public Supermercado getSupermercado() {
        return supermercado;
    }
}
