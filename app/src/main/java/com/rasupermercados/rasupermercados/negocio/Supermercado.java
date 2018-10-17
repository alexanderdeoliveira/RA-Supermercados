package com.rasupermercados.rasupermercados.negocio;

public class Supermercado {
    int codigo;
    String nome;
    String urlImagem;

    public Supermercado(int codigo) {
        setCodigo(codigo);
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public int getCodigo() {
        return codigo;
    }
}
