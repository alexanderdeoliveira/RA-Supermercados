package com.rasupermercados.rasupermercados.listies.viewholders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;
import com.rasupermercados.rasupermercados.R;
import com.rasupermercados.rasupermercados.activities.MainActivity;
import com.rasupermercados.rasupermercados.negocio.Categoria;
import com.rasupermercados.rasupermercados.negocio.Supermercado;

public class LogoSupermercadoViewHolder extends RecyclerView.ViewHolder{

    private ImageView ivImagemSupermercado;

    private Context contexto;
    public LogoSupermercadoViewHolder(Context context, View itemView) {
        super(itemView);
        this.contexto = context;

        ivImagemSupermercado = itemView.findViewById(R.id.iv_imagem_categoria);

    }

    public void setSupermercado(final Supermercado supermercado, StorageReference mStorageCategoria) {
        StorageReference supermecadoRef = mStorageCategoria.child(supermercado.getUrlImagem());

        Glide.with(contexto)
                .using(new FirebaseImageLoader())
                .load(supermecadoRef)
                .into(ivImagemSupermercado);

    }
}
