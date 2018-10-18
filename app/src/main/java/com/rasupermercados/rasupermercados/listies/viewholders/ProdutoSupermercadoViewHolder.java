package com.rasupermercados.rasupermercados.listies.viewholders;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.rasupermercados.rasupermercados.R;
import com.rasupermercados.rasupermercados.fragments.ConfirmarProdutoDialog;
import com.rasupermercados.rasupermercados.negocio.Produto;
import com.rasupermercados.rasupermercados.negocio.ProdutoSupermercado;
import com.rasupermercados.rasupermercados.utils.EnumeradoresUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProdutoSupermercadoViewHolder extends RecyclerView.ViewHolder {

    private TextView tvValorProduto;
    private TextView tvNomeSupermercado;
    private AppCompatSpinner spinnerQtd;
    private DatabaseReference refSupermercado;
    private FragmentManager fragmentManager;

    private Context contexto;
    private int codProduto;

    public ProdutoSupermercadoViewHolder(Context context, View itemView, FragmentManager fragmentManager, int codProduto) {
        super(itemView);
        this.contexto = context;
        this.codProduto = codProduto;
        this.fragmentManager = fragmentManager;
        tvValorProduto = itemView.findViewById(R.id.tv_valor_produto);
        tvNomeSupermercado = itemView.findViewById(R.id.tv_nome_supermercado);
        spinnerQtd = itemView.findViewById(R.id.sp_qtd);
        refSupermercado = FirebaseDatabase.getInstance().getReference("supermercados");
    }

    public void setItem(final ProdutoSupermercado item) {
        final DecimalFormat df = new DecimalFormat("#0.00");

        DatabaseReference ref = refSupermercado.child(Integer.toString(item.getSupermercado().getCodigo())).child("nome");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tvNomeSupermercado.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        tvValorProduto.setText("R$ " + df.format(item.getValor()));
        tvNomeSupermercado.setText(item.getSupermercado().getNome());

        spinnerQtd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0) {
                    ConfirmarProdutoDialog dialog = new ConfirmarProdutoDialog();
                    Bundle extras = new Bundle();
                    extras.putInt("codigoProduto", codProduto);
                    extras.putInt("quantidade", position);
                    extras.putDouble("valorProduto", item.getValor());
                    extras.putInt("codigoSupermercado", item.getSupermercado().getCodigo());
                    extras.putString("nomeSupermercado", tvNomeSupermercado.getText().toString());

                    dialog.setArguments(extras);

                    dialog.show(fragmentManager, "frag_confirmar_produto");

                    spinnerQtd.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        preencherQtd();
    }

    private void preencherQtd() {
        List<String> listaQtds = new ArrayList<>();
   /*     double acrescimo = 0;
        if(produto.getCategoria().getCodigoCategoria() == EnumeradoresUtils.TIPO_CATEGORIA.ACOUGUE.getStatusCode()
                || produto.getCategoria().getCodigoCategoria() == EnumeradoresUtils.TIPO_CATEGORIA.HORTIFRUTI.getStatusCode()) {
            acrescimo = 0.1;
        } else
            acrescimo = 1;*/

        for(int i=0; i<10;i++) {
            listaQtds.add(Integer.toString(i));
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(itemView.getContext(), android.R.layout.simple_spinner_item, listaQtds);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQtd.setAdapter(dataAdapter);
    }
}
