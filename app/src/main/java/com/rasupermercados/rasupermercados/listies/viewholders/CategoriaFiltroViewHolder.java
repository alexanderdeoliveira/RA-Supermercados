package com.rasupermercados.rasupermercados.listies.viewholders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.rasupermercados.rasupermercados.R;
import com.rasupermercados.rasupermercados.activities.MainActivity;
import com.rasupermercados.rasupermercados.listies.adapters.AdapterListaCategorias;
import com.rasupermercados.rasupermercados.listies.adapters.AdapterListaCategoriasFiltros;
import com.rasupermercados.rasupermercados.negocio.Categoria;

public class CategoriaFiltroViewHolder extends RecyclerView.ViewHolder{

    private CheckBox cbCategoria;
    private TextView tvNomeCategoria;
    private AdapterListaCategoriasFiltros adapterListaCategoriasFiltros;

    private Context contexto;
    public CategoriaFiltroViewHolder(Context context, View itemView, AdapterListaCategoriasFiltros adapterListaCategoriasFiltros) {
        super(itemView);
        this.contexto = context;
        this.adapterListaCategoriasFiltros = adapterListaCategoriasFiltros;
        cbCategoria = itemView.findViewById(R.id.cb_categoria);
        tvNomeCategoria = itemView.findViewById(R.id.tv_nome_categoria);
    }

    public void setCategoria(final Categoria categoria) {
        tvNomeCategoria.setText(categoria.getNome());

        cbCategoria.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    adapterListaCategoriasFiltros.listaFiltros.add(categoria);
                else
                    adapterListaCategoriasFiltros.listaFiltros.remove(categoria);
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbCategoria.isChecked())
                    cbCategoria.setChecked(false);
                else
                    cbCategoria.setChecked(true);
            }
        });

    }
}
