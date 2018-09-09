package com.rasupermercados.rasupermercados.negocio;

public class Supermercado {
    int codigo;
    String nome;

    public void setNome(String nome) {
        this.nome = nome;
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
