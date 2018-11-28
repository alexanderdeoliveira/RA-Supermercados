package com.rasupermercados.rasupermercados.listies.viewholders;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rasupermercados.rasupermercados.R;
import com.rasupermercados.rasupermercados.db.CarrinhoDB;
import com.rasupermercados.rasupermercados.fragments.ConfirmarProdutoDialog;
import com.rasupermercados.rasupermercados.listies.adapters.AdapterListaProdutosSupermercadoCarrinho;
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
    private Spinner spQtdeProduto;
    private TextView tvNomeSupermercado;
    private DatabaseReference refSupermercado;
    private AdapterListaProdutosSupermercadoCarrinho mAdapter;

    private Context contexto;
    public ProdutoSupermercadoCarrinhoViewHolder(Context context, View itemView, AdapterListaProdutosSupermercadoCarrinho mAdapter) {
        super(itemView);
        this.contexto = context;
        this.mAdapter = mAdapter;

        tvValorProduto = itemView.findViewById(R.id.tv_valor_produto);
        tvNomeSupermercado = itemView.findViewById(R.id.tv_nome_supermercado);
        tvNomeProduto = itemView.findViewById(R.id.tv_nome_produto);
        tvValorTotal = itemView.findViewById(R.id.tv_valor_total);
        spQtdeProduto = itemView.findViewById(R.id.sp_qtd);
        refSupermercado = FirebaseDatabase.getInstance().getReference("supermercados");
    }

    public void setItem(final ProdutoSupermercadoCarrinho item, final int position) {
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
        final DecimalFormat df = new DecimalFormat("#0.00");
        tvValorProduto.setText("R$ " + df.format(item.getProdutoSupermercado().getValor()));
        tvValorTotal.setText("R$ " + df.format(item.getQuantidade() * item.getProdutoSupermercado().getValor()));
        tvNomeProduto.setText(item.getProduto().getNome());

        spQtdeProduto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int positionQtd, long id) {
                if(positionQtd > 0) {
                    if(positionQtd != item.getQuantidade()) {
                        item.setQuantidade(positionQtd);
                        mAdapter.alterarItem(position, item);
                    }
                } else
                    mAdapter.removeItem(position, item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        preencherQtd();
        spQtdeProduto.setSelection(item.getQuantidade());
    }

    private void preencherQtd() {
        List<String> listaQtds = new ArrayList<>();

        for(int i=0; i<10;i++) {
            listaQtds.add(Integer.toString(i));
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(itemView.getContext(), android.R.layout.simple_spinner_item, listaQtds);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spQtdeProduto.setAdapter(dataAdapter);
    }
}
