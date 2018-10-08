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
import com.rasupermercados.rasupermercados.activities.ProdutosActivity;
import com.rasupermercados.rasupermercados.negocio.Categoria;

public class CategoriaViewHolder extends RecyclerView.ViewHolder{

    private ImageView ivImagemCategoria;
    private TextView tvNomeCategoria;

    private Context contexto;
    public CategoriaViewHolder(Context context, View itemView) {
        super(itemView);
        this.contexto = context;

        ivImagemCategoria = itemView.findViewById(R.id.iv_imagem_categoria);
        tvNomeCategoria = itemView.findViewById(R.id.tv_nome_categoria);

    }

    public void setCategoria(final Categoria categoria, StorageReference mStorageCategoria) {
        StorageReference categoriaRef = mStorageCategoria.child(categoria.getUrlFotoCategoria());
        tvNomeCategoria.setText(categoria.getNome());

        Glide.with(contexto)
                .using(new FirebaseImageLoader())
                .load(categoriaRef)
                .into(ivImagemCategoria);

        /*final long ONE_MEGABYTE = 1024 * 1024;
        produtoRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Drawable imagemCategoria = new BitmapDrawable(contexto.getResources(), BitmapFactory.decodeByteArray(bytes, 0, bytes.length));

                ivImagemCategoria.setImageDrawable(imagemCategoria);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });*/

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(contexto, ProdutosActivity.class);
                intent.putExtra("codigoCategoria", categoria.getCodigoCategoria());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                contexto.startActivity(intent);
            }
        });

    }
}
