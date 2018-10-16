package com.rasupermercados.rasupermercados.listies.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.rasupermercados.rasupermercados.R;
import com.rasupermercados.rasupermercados.negocio.Produto;
import com.rasupermercados.rasupermercados.negocio.ProdutoSupermercado;
import com.rasupermercados.rasupermercados.negocio.ProdutoSupermercadoCarrinho;
import com.rasupermercados.rasupermercados.utils.EnumeradoresUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProdutoSupermercadoCarrinhoViewHolder extends RecyclerView.ViewHolder{

    private TextView tvValorProduto;
    private TextView tvValorTotal;
    private TextView tvNomeProduto;
    private TextView tvQtdeProduto;
    private TextView tvNomeSupermercado;

    private Context contexto;
    public ProdutoSupermercadoCarrinhoViewHolder(Context context, View itemView) {
        super(itemView);
        this.contexto = context;

        tvValorProduto = itemView.findViewById(R.id.tv_valor_produto);
        tvNomeSupermercado = itemView.findViewById(R.id.tv_nome_supermercado);
        tvNomeProduto = itemView.findViewById(R.id.tv_nome_produto);
        tvValorTotal = itemView.findViewById(R.id.tv_valor_total);
        tvQtdeProduto = itemView.findViewById(R.id.tv_qtd_produto);
    }

    public void setItem(ProdutoSupermercadoCarrinho item) {
        DecimalFormat df = new DecimalFormat("#0.00");
        tvValorProduto.setText("R$ " + df.format(item.getProdutoSupermercado().getValor()));
        tvValorTotal.setText("R$ " + df.format(item.getQuantidade() * item.getProdutoSupermercado().getValor()));
        tvNomeSupermercado.setText(item.getProdutoSupermercado().getSupermercado().getNome());
        tvNomeProduto.setText(item.getProdutoSupermercado().getProduto().getNome());
        tvQtdeProduto.setText(Double.toString(item.getQuantidade()));
        }
}
