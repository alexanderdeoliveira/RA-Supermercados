package com.rasupermercados.rasupermercados.negocio;

import java.util.ArrayList;
import java.util.List;

public class Produto {
    int codigo;
    String nome;
    Categoria categoria;
    List<ProdutoSupermercado> produtosSupermercado;
    String urlFotoStorage;

    public Produto(int codigo, String nome) {
        setCodigo(codigo);
        setNome(nome);
    }

    public Produto(ProdutoFirebase produtoFirebase) {
        setCodigo(produtoFirebase.getCodigo());
        setUrlFotoStorage(produtoFirebase.getUrlFotoStorage());
        setCategoria(new Categoria(produtoFirebase.getCategoria().codigoCategoria));
        setNome(produtoFirebase.getNome());

        for(int i=0;i<produtoFirebase.getSupermercados().size();i++) {
            SupermercadoFirebase supermercadoFirebase = produtoFirebase.getSupermercados().get(i);

            ProdutoSupermercado produtoSupermercado = new ProdutoSupermercado(new Supermercado(supermercadoFirebase.codigo), supermercadoFirebase.getValor());
            addProdutoSupermercado(produtoSupermercado);
        }

    }

    public void setProdutosSupermercado(List<ProdutoSupermercado> produtosSupermercado) {
        this.produtosSupermercado = produtosSupermercado;
    }

    public List<ProdutoSupermercado> getProdutosSupermercado() {
        return produtosSupermercado;
    }

    public Produto() {}

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

    public void addProdutoSupermercado(ProdutoSupermercado produtoSupermercado) {
        if(produtosSupermercado == null)
            this.produtosSupermercado = new ArrayList<>();

        this.produtosSupermercado.add(produtoSupermercado);
    }

    public String getUrlFotoStorage() {
        return urlFotoStorage;
    }

    public void setUrlFotoStorage(String urlFotoStorage) {
        this.urlFotoStorage = urlFotoStorage;
    }
}
