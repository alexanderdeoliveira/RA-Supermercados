package com.rasupermercados.rasupermercados.listies.viewholders;

import android.content.Context;
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
import com.google.firebase.storage.StorageReference;
import com.rasupermercados.rasupermercados.R;
import com.rasupermercados.rasupermercados.negocio.Produto;
import com.rasupermercados.rasupermercados.negocio.ProdutoSupermercado;

import java.text.DecimalFormat;

public class ProdutoSupermercadoViewHolder extends RecyclerView.ViewHolder{

    private TextView tvValorProduto;
    private TextView tvNomeSupermercado;

    private Context contexto;
    public ProdutoSupermercadoViewHolder(Context context, View itemView) {
        super(itemView);
        this.contexto = context;

        tvValorProduto = itemView.findViewById(R.id.tv_valor_produto);
        tvNomeSupermercado = itemView.findViewById(R.id.tv_nome_supermercado);

    }

    public void setItem(ProdutoSupermercado item) {
        DecimalFormat df = new DecimalFormat("#0.00");

        tvValorProduto.setText("R$ " + df.format(item.getValor()));
        tvNomeSupermercado.setText(item.getSupermercado().getNome());
    }
}
