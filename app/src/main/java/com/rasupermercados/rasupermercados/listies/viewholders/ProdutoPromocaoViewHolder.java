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
import com.rasupermercados.rasupermercados.activities.DetalhesProdutoActivity;
import com.rasupermercados.rasupermercados.negocio.Produto;

public class ProdutoPromocaoViewHolder extends RecyclerView.ViewHolder{

    private ImageView ivProduto;
    private TextView tvNomeProduto;

    private Context contexto;
    public ProdutoPromocaoViewHolder(Context context, View itemView) {
        super(itemView);
        this.contexto = context;

        ivProduto = itemView.findViewById(R.id.iv_produto);
        tvNomeProduto = itemView.findViewById(R.id.tv_nome_produto);

    }

    public void setProduto(final Produto produto, StorageReference mStorageProdutos) {
        StorageReference produtoRef = mStorageProdutos.child(produto.getUrlFotoStorage());
        tvNomeProduto.setText(produto.getNome());

        Glide.with(contexto)
                .using(new FirebaseImageLoader())
                .load(produtoRef)
                .into(ivProduto);

    }
}
