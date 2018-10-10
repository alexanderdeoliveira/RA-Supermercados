package com.rasupermercados.rasupermercados.listies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.rasupermercados.rasupermercados.R;
import com.rasupermercados.rasupermercados.listies.viewholders.CategoriaFiltroViewHolder;
import com.rasupermercados.rasupermercados.negocio.Categoria;

import java.util.ArrayList;
import java.util.List;

public class AdapterListaCategoriasFiltros extends RecyclerView.Adapter<CategoriaFiltroViewHolder> {
    private List<Categoria> categorias;
    private Context contexto;
    public List<Categoria> listaFiltros;

    public AdapterListaCategoriasFiltros(Context contexto, List<Categoria> categorias) {
        this.categorias = categorias;
        this.contexto = contexto;
        this.listaFiltros = new ArrayList<>();

    }
    @NonNull
    @Override
    public CategoriaFiltroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_categoria_filtros, parent, false);

        CategoriaFiltroViewHolder categoriaViewHolder = new CategoriaFiltroViewHolder(contexto, view, this);

        return categoriaViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaFiltroViewHolder holder, int position) {
        holder.setCategoria(categorias.get(position));
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }
}
