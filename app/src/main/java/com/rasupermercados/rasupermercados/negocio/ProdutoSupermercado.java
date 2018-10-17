package com.rasupermercados.rasupermercados.negocio;

import android.support.annotation.NonNull;

public class ProdutoSupermercado {
    Supermercado supermercado;
    double valor;

    public ProdutoSupermercado(Supermercado supermercado, double valor) {
        setSupermercado(supermercado);
        setValor(valor);
    }

    public void setValor(double valor) {
        this.valor = valor;
    }


    public void setSupermercado(Supermercado supermercado) {
        this.supermercado = supermercado;
    }

    public double getValor() {
        return valor;
    }

    public Supermercado getSupermercado() {
        return supermercado;
    }
}
