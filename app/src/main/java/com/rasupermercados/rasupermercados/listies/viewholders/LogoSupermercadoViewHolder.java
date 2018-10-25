package com.rasupermercados.rasupermercados.listies.viewholders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rasupermercados.rasupermercados.R;
import com.rasupermercados.rasupermercados.activities.MainActivity;
import com.rasupermercados.rasupermercados.negocio.Categoria;
import com.rasupermercados.rasupermercados.negocio.Supermercado;
import com.rasupermercados.rasupermercados.negocio.SupermercadoCarrinho;

public class LogoSupermercadoViewHolder extends RecyclerView.ViewHolder{

    private ImageView ivImagemSupermercado;
    private DatabaseReference refSupermercado;
    private StorageReference mStorageSupermercados;

    private Context contexto;
    public LogoSupermercadoViewHolder(Context context, View itemView) {
        super(itemView);
        this.contexto = context;

        ivImagemSupermercado = itemView.findViewById(R.id.iv_imagem_supermercado);
        refSupermercado = FirebaseDatabase.getInstance().getReference("supermercados");
        mStorageSupermercados = FirebaseStorage.getInstance().getReference().child("supermercados");
    }

    public void setSupermercadoCarrinho(SupermercadoCarrinho supermercadoCarrinho) {
        DatabaseReference ref = refSupermercado.child(Integer.toString(supermercadoCarrinho.getSupermercado().getCodigo())).child("urlImagem");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                StorageReference supermercadoRef = mStorageSupermercados.child(dataSnapshot.getValue(String.class));

                Glide.with(contexto)
                        .using(new FirebaseImageLoader())
                        .load(supermercadoRef)
                        .into(ivImagemSupermercado);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }
}
