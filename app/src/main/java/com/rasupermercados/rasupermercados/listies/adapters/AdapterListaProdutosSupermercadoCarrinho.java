package com.rasupermercados.rasupermercados.listies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.storage.StorageReference;
import com.rasupermercados.rasupermercados.R;
import com.rasupermercados.rasupermercados.listies.viewholders.ProdutoSupermercadoCarrinhoViewHolder;
import com.rasupermercados.rasupermercados.listies.viewholders.ProdutoSupermercadoViewHolder;
import com.rasupermercados.rasupermercados.negocio.ProdutoSupermercado;
import com.rasupermercados.rasupermercados.negocio.ProdutoSupermercadoCarrinho;

import java.util.List;

public class AdapterListaProdutosSupermercadoCarrinho extends RecyclerView.Adapter<ProdutoSupermercadoCarrinhoViewHolder> {
    private List<ProdutoSupermercadoCarrinho> itens;
    private Context contexto;

    public AdapterListaProdutosSupermercadoCarrinho(Context contexto, List<ProdutoSupermercadoCarrinho> itens) {
        this.itens = itens;
        this.contexto = contexto;
    }
    @NonNull
    @Override
    public ProdutoSupermercadoCarrinhoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_produto_supermercado, parent, false);

        ProdutoSupermercadoCarrinhoViewHolder produtoViewHolder = new ProdutoSupermercadoCarrinhoViewHolder(contexto, view);

        return produtoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoSupermercadoCarrinhoViewHolder holder, int position) {
        holder.setItem(itens.get(position));
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }
}
