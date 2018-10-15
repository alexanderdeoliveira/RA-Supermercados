package com.rasupermercados.rasupermercados.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.rasupermercados.rasupermercados.R;
import com.rasupermercados.rasupermercados.listies.adapters.AdapterListaLogoSupermercado;
import com.rasupermercados.rasupermercados.negocio.Supermercado;

public class CarrinhoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView rvSupermercados = findViewById(R.id.rv_supercados);
        AdapterListaLogoSupermercado mAdapterProdutPromocao = new AdapterListaLogoSupermercado(this, Supermercado)

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvSupermercados.setLayoutManager(layoutManager);
        rvSupermercados.setAdapter(mAdapterProdutPromocao);


    }

}
