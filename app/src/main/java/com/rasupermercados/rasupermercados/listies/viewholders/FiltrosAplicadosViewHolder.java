package com.rasupermercados.rasupermercados.listies.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.rasupermercados.rasupermercados.R;
import com.rasupermercados.rasupermercados.listies.adapters.AdapterListaCategoriasFiltros;
import com.rasupermercados.rasupermercados.negocio.Categoria;

public class FiltrosAplicadosViewHolder extends RecyclerView.ViewHolder{

    private Button btCategoria;


    private Context contexto;
    public FiltrosAplicadosViewHolder(Context context, View itemView) {
        super(itemView);
        this.contexto = context;

        btCategoria = itemView.findViewById(R.id.bt_filtro_aplicado);

    }

    public void setFiltro(final Categoria categoria) {
        btCategoria.setText(categoria.getNome());
    }
}
