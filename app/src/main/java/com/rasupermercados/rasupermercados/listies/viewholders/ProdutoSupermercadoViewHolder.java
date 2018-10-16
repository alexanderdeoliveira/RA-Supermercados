package com.rasupermercados.rasupermercados.listies.viewholders;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.rasupermercados.rasupermercados.R;
import com.rasupermercados.rasupermercados.negocio.Produto;
import com.rasupermercados.rasupermercados.negocio.ProdutoSupermercado;
import com.rasupermercados.rasupermercados.utils.EnumeradoresUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProdutoSupermercadoViewHolder extends RecyclerView.ViewHolder{

    private TextView tvValorProduto;
    private TextView tvNomeSupermercado;
    private Spinner spinnerQtd;

    private Context contexto;
    public ProdutoSupermercadoViewHolder(Context context, View itemView) {
        super(itemView);
        this.contexto = context;

        tvValorProduto = itemView.findViewById(R.id.tv_valor_produto);
        tvNomeSupermercado = itemView.findViewById(R.id.tv_nome_supermercado);
        spinnerQtd = itemView.findViewById(R.id.sp_qtd);


    }

    public void setItem(ProdutoSupermercado item) {
        DecimalFormat df = new DecimalFormat("#0.00");

        tvValorProduto.setText("R$ " + df.format(item.getValor()));
        tvNomeSupermercado.setText(item.getSupermercado().getNome());

        preencherQtd(item.getProduto());
    }

    private void preencherQtd(Produto produto) {
        List<String> listaQtds = new ArrayList<>();
        double acrescimo = 0;
        if(produto.getCategoria().getCodigoCategoria() == EnumeradoresUtils.TIPO_CATEGORIA.ACOUGUE.getStatusCode()
                || produto.getCategoria().getCodigoCategoria() == EnumeradoresUtils.TIPO_CATEGORIA.HORTIFRUTI.getStatusCode()) {
            acrescimo = 0.1;
        } else
            acrescimo = 1;

        for(double i=0; i<5;i = i+acrescimo) {
            listaQtds.add(Double.toString(i));
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(itemView.getContext(), android.R.layout.simple_spinner_item, listaQtds);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQtd.setAdapter(dataAdapter);
    }
}
