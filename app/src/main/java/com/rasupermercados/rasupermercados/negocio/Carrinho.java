package com.rasupermercados.rasupermercados.negocio;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Carrinho {
    List<ProdutoSupermercadoCarrinho> produtoSupermercadoCarrinhos;

    public Carrinho() {
        this.produtoSupermercadoCarrinhos = new ArrayList<>();
    }

    public String getValorTotal() {
        double valorTotal = 0;
        for(int i = 0; i< produtoSupermercadoCarrinhos.size();i++) {
            valorTotal += (produtoSupermercadoCarrinhos.get(i).getQuantidade() * produtoSupermercadoCarrinhos.get(i).getProdutoSupermercado().getValor());
        }

        DecimalFormat df = new DecimalFormat("#0.00");
        return "R$ " + (df.format(valorTotal).replace(".", ","));

    }

    public void addProdutoSupermercadoCarrinho(ProdutoSupermercadoCarrinho produtoSupermercadoCarrinho) {
        this.produtoSupermercadoCarrinhos.add(produtoSupermercadoCarrinho);

    }

    public void addProdutoSupermercadoCarrinho(ProdutoSupermercadoCarrinho produtoSupermercadoCarrinho, int position) {
        this.produtoSupermercadoCarrinhos.add(position, produtoSupermercadoCarrinho);

    }

    public void removeProdutoSupermercadoCarrinho(int position) {
        this.produtoSupermercadoCarrinhos.remove(position);
    }

    public void alterarProdutoSupermercadoCarrinho(int position, ProdutoSupermercadoCarrinho produtoSupermercadoCarrinhoAlterado) {
        ProdutoSupermercadoCarrinho produtoSupermercadoCarrinho = this.produtoSupermercadoCarrinhos.get(position);
        produtoSupermercadoCarrinho.setQuantidade(produtoSupermercadoCarrinhoAlterado.getQuantidade());
        produtoSupermercadoCarrinho.setProdutoSupermercado(produtoSupermercadoCarrinhoAlterado.getProdutoSupermercado());
        produtoSupermercadoCarrinho.setProduto(produtoSupermercadoCarrinho.produto);
    }


    public void setProdutoSupermercadoCarrinhos(List<ProdutoSupermercadoCarrinho> produtoSupermercadoCarrinhos) {
        this.produtoSupermercadoCarrinhos = produtoSupermercadoCarrinhos;
    }

    public List<ProdutoSupermercadoCarrinho> getProdutoSupermercadoCarrinhos() {
        return produtoSupermercadoCarrinhos;
    }
}
