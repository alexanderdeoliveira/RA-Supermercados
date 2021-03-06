package com.rasupermercados.rasupermercados.listies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rasupermercados.rasupermercados.R;
import com.rasupermercados.rasupermercados.listies.viewholders.ProdutoViewHolder;
import com.rasupermercados.rasupermercados.negocio.Produto;

import java.util.List;

public class AdapterListaProdutos extends RecyclerView.Adapter<ProdutoViewHolder> {
    private List<Produto> produtos;
    private Context contexto;
    private StorageReference mStorageProdutos;

    public AdapterListaProdutos(Context contexto, List<Produto> produtos) {
        this.produtos = produtos;
        this.contexto = contexto;
        mStorageProdutos = FirebaseStorage.getInstance().getReference().child("produtos");

    }

    public void atualizarListaProdutos(List<Produto> produtos) {
        this.produtos = produtos;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_produto, parent, false);

        ProdutoViewHolder produtoViewHolder = new ProdutoViewHolder(contexto, view);

        return produtoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoViewHolder holder, int position) {
        holder.setProduto(produtos.get(position), mStorageProdutos);
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }
}
