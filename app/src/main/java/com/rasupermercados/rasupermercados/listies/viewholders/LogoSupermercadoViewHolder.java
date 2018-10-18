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

public class LogoSupermercadoViewHolder extends RecyclerView.ViewHolder{

    private ImageView ivImagemSupermercado;
    private DatabaseReference refSupermercado;
    private StorageReference mStorageSupemercado;

    private Context contexto;
    public LogoSupermercadoViewHolder(Context context, View itemView) {
        super(itemView);
        this.contexto = context;

        ivImagemSupermercado = itemView.findViewById(R.id.iv_imagem_categoria);
        refSupermercado = FirebaseDatabase.getInstance().getReference("supermercados");
        mStorageSupemercado = FirebaseStorage.getInstance().getReference().child("supermercados");
    }

    public void setSupermercado(Supermercado supermercado) {
        DatabaseReference ref = refSupermercado.child(Integer.toString(supermercado.getCodigo())).child("urlImagem");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                StorageReference supermecadoRef = mStorageSupemercado.child(dataSnapshot.getValue(String.class));

                Glide.with(contexto)
                        .using(new FirebaseImageLoader())
                        .load(supermecadoRef)
                        .into(ivImagemSupermercado);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }
}
