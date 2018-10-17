package com.rasupermercados.rasupermercados.negocio;

import java.util.ArrayList;
import java.util.List;

public class ProdutoFirebase {
    int codigo;
    String nome;
    CategoriaFirebase categoria;
    List<SupermercadoFirebase> supermercados;
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

    public void setCategoria(CategoriaFirebase categoria) {
        this.categoria = categoria;
    }

    public CategoriaFirebase getCategoria() {
        return categoria;
    }

    public String getUrlFotoStorage() {
        return urlFotoStorage;
    }

    public void setUrlFotoStorage(String urlFotoStorage) {
        this.urlFotoStorage = urlFotoStorage;
    }

    public List<SupermercadoFirebase> getSupermercados() {
        return supermercados;
    }

    public void addSupermercado(SupermercadoFirebase supermercadoFirebase){
        if(this.supermercados == null)
            this.supermercados = new ArrayList<>();

        this.supermercados.add(supermercadoFirebase);
    }

}
