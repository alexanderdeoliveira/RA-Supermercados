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
import com.rasupermercados.rasupermercados.listies.viewholders.FiltrosAplicadosViewHolder;
import com.rasupermercados.rasupermercados.negocio.Categoria;

import java.util.List;

public class AdapterListaFiltrosAplicados extends RecyclerView.Adapter<FiltrosAplicadosViewHolder> {
    private List<Categoria> categorias;
    private Context contexto;

    public AdapterListaFiltrosAplicados(Context contexto, List<Categoria> categorias) {
        this.categorias = categorias;
        this.contexto = contexto;

    }

    public void addItem(Categoria categoria) {
        categorias.add(categoria);
        notifyItemInserted(categorias.size()-1);
    }

    @NonNull
    @Override
    public FiltrosAplicadosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_filtros_aplicados, parent, false);

        FiltrosAplicadosViewHolder filtrosAplicadosViewHolder = new FiltrosAplicadosViewHolder(contexto, view);

        return filtrosAplicadosViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FiltrosAplicadosViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }
}