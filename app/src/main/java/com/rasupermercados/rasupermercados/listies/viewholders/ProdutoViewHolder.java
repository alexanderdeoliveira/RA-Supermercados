package com.rasupermercados.rasupermercados.listies.viewholders;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;
import com.rasupermercados.rasupermercados.R;
import com.rasupermercados.rasupermercados.activities.DetalhesProdutoActivity;
import com.rasupermercados.rasupermercados.negocio.Produto;

import java.io.File;

public class ProdutoViewHolder extends RecyclerView.ViewHolder{

    private ImageView ivProduto;
    private TextView tvNomeProduto;

    private Context contexto;
    public ProdutoViewHolder(Context context, View itemView) {
        super(itemView);
        this.contexto = context;

        ivProduto = itemView.findViewById(R.id.iv_produto);
        tvNomeProduto = itemView.findViewById(R.id.tv_nome_produto);

    }

    public void setProduto(final Produto produto, StorageReference mStorageProdutos) {

        StorageReference produtoRef = mStorageProdutos.child(produto.getUrlFotoStorage());
        tvNomeProduto.setText(produto.getNome());

        final long ONE_MEGABYTE = 1024 * 1024;
        produtoRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Drawable imagemProduto = new BitmapDrawable(contexto.getResources(), BitmapFactory.decodeByteArray(bytes, 0, bytes.length));

                ivProduto.setImageDrawable(imagemProduto);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(contexto, DetalhesProdutoActivity.class);
                intent.putExtra("codigoProduto", produto.getCodigo());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                contexto.startActivity(intent);
            }
        });

    }
}
