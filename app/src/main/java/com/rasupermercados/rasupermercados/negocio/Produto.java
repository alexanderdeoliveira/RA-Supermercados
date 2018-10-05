package com.rasupermercados.rasupermercados.negocio;

public class Produto {
    int codigo;
    String nome;
    Categoria categoria;
    String urlFotoStorage;

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public String getUrlFotoStorage() {
        return urlFotoStorage;
    }

    public void setUrlFotoStorage(String urlFotoStorage) {
        this.urlFotoStorage = urlFotoStorage;
    }
}
