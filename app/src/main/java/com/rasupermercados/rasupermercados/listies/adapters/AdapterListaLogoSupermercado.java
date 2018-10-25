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
import com.rasupermercados.rasupermercados.listies.viewholders.LogoSupermercadoViewHolder;
import com.rasupermercados.rasupermercados.negocio.Categoria;
import com.rasupermercados.rasupermercados.negocio.Supermercado;
import com.rasupermercados.rasupermercados.negocio.SupermercadoCarrinho;

import java.util.List;

public class AdapterListaLogoSupermercado extends RecyclerView.Adapter<LogoSupermercadoViewHolder> {
    private List<SupermercadoCarrinho> supermercados;
    private Context contexto;

    public AdapterListaLogoSupermercado(Context contexto, List<SupermercadoCarrinho> supermercados) {
        this.supermercados = supermercados;
        this.contexto = contexto;

    }
    @NonNull
    @Override
    public LogoSupermercadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_logo_supermercado, parent, false);

        LogoSupermercadoViewHolder lgoSupermercadoViewHolder = new LogoSupermercadoViewHolder(contexto, view);

        return lgoSupermercadoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LogoSupermercadoViewHolder holder, int position) {
        holder.setSupermercadoCarrinho(supermercados.get(position));
    }

    @Override
    public int getItemCount() {
        return supermercados.size();
    }
}
