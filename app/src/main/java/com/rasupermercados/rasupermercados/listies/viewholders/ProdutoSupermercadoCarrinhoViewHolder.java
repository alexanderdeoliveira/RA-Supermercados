package com.rasupermercados.rasupermercados.listies.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    private DatabaseReference refSupermercado;

    private Context contexto;
    public ProdutoSupermercadoCarrinhoViewHolder(Context context, View itemView) {
        super(itemView);
        this.contexto = context;

        tvValorProduto = itemView.findViewById(R.id.tv_valor_produto);
        tvNomeSupermercado = itemView.findViewById(R.id.tv_nome_supermercado);
        tvNomeProduto = itemView.findViewById(R.id.tv_nome_produto);
        tvValorTotal = itemView.findViewById(R.id.tv_valor_total);
        tvQtdeProduto = itemView.findViewById(R.id.tv_qtd_produto);
        refSupermercado = FirebaseDatabase.getInstance().getReference("supermercados");
    }

    public void setItem(ProdutoSupermercadoCarrinho item) {
        DatabaseReference ref = refSupermercado.child(Integer.toString(item.getProdutoSupermercado().getSupermercado().getCodigo())).child("nome");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tvNomeSupermercado.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        DecimalFormat df = new DecimalFormat("#0.00");
        tvValorProduto.setText("R$ " + df.format(item.getProdutoSupermercado().getValor()));
        tvValorTotal.setText("R$ " + df.format(item.getQuantidade() * item.getProdutoSupermercado().getValor()));
        tvNomeProduto.setText(item.getProduto().getNome());
        tvQtdeProduto.setText(Integer.toString(item.getQuantidade()));
        }
}
