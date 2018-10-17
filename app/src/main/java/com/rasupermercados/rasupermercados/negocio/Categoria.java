package com.rasupermercados.rasupermercados.negocio;

import java.util.List;

public class Categoria {
    int codigoCategoria;
    String nome;

    public Categoria(){}

    public Categoria(int codigoCategoria) {
        setCodigoCategoria(codigoCategoria);
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCodigoCategoria(int codigoCategoria) {
        this.codigoCategoria = codigoCategoria;
    }

    public String getNome() {
        return nome;
    }

    public int getCodigoCategoria() {
        return codigoCategoria;
    }
}
