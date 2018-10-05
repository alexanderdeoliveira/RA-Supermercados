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
import com.rasupermercados.rasupermercados.listies.viewholders.CategoriaViewHolder;
import com.rasupermercados.rasupermercados.listies.viewholders.ProdutoViewHolder;
import com.rasupermercados.rasupermercados.negocio.Categoria;
import com.rasupermercados.rasupermercados.negocio.Produto;

import java.util.List;

public class AdapterListaCategorias extends RecyclerView.Adapter<CategoriaViewHolder> {
    private List<Categoria> categorias;
    private Context contexto;
    private StorageReference mStorageCategorias;

    public AdapterListaCategorias(Context contexto, List<Categoria> categorias) {
        this.categorias = categorias;
        this.contexto = contexto;
        mStorageCategorias = FirebaseStorage.getInstance().getReference().child("categorias");

    }
    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_categoria, parent, false);

        CategoriaViewHolder categoriaViewHolder = new CategoriaViewHolder(contexto, view);

        return categoriaViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaViewHolder holder, int position) {
        holder.setCategoria(categorias.get(position), mStorageCategorias);
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }
}
