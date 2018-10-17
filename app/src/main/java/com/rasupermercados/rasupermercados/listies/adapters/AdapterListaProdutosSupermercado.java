package com.rasupermercados.rasupermercados.listies.adapters;

import android.app.Fragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rasupermercados.rasupermercados.R;
import com.rasupermercados.rasupermercados.listies.viewholders.ProdutoSupermercadoViewHolder;
import com.rasupermercados.rasupermercados.listies.viewholders.ProdutoViewHolder;
import com.rasupermercados.rasupermercados.negocio.Produto;
import com.rasupermercados.rasupermercados.negocio.ProdutoSupermercado;

import java.util.List;

public class AdapterListaProdutosSupermercado extends RecyclerView.Adapter<ProdutoSupermercadoViewHolder> {
    private List<ProdutoSupermercado> itens;
    private Context contexto;
    private StorageReference mStorage;
    private FragmentManager fragmentManager;
    private int codProduto;

    public AdapterListaProdutosSupermercado(Context contexto, Produto produto, FragmentManager fm) {
        this.itens = produto.getProdutosSupermercado();
        this.contexto = contexto;
        this.fragmentManager = fm;
        this.codProduto = produto.getCodigo();

    }
    @NonNull
    @Override
    public ProdutoSupermercadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_produto_supermercado, parent, false);

        ProdutoSupermercadoViewHolder produtoViewHolder = new ProdutoSupermercadoViewHolder(contexto, view,  fragmentManager, codProduto);

        return produtoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoSupermercadoViewHolder holder, int position) {
        holder.setItem(itens.get(position));
    }

    public void addItem(ProdutoSupermercado item) {
        itens.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }
}
