package com.rasupermercados.rasupermercados.negocio;

import java.util.List;

public class Categoria {
    int codigoCategoria;
    String nome;
    String urlFotoCategoria;

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

    public void setUrlFotoCategoria(String urlFotoCategoria) {
        this.urlFotoCategoria = urlFotoCategoria;
    }

    public String getUrlFotoCategoria() {
        return urlFotoCategoria;
    }
}
