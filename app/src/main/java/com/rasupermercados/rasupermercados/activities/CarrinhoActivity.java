package com.rasupermercados.rasupermercados.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.rasupermercados.rasupermercados.R;
import com.rasupermercados.rasupermercados.db.CarrinhoDB;
import com.rasupermercados.rasupermercados.listies.adapters.AdapterListaProdutosSupermercadoCarrinho;
import com.rasupermercados.rasupermercados.negocio.Carrinho;

import java.text.DecimalFormat;

public class CarrinhoActivity extends AppCompatActivity {

    private RecyclerView rvProdutosSupermercado;
    private RecyclerView rvItensCarrinho;
    private TextView tvValorTotalCarrinho;
    private Carrinho carrinho;
    private AdapterListaProdutosSupermercadoCarrinho mAdapterItensCarrinho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_produto_2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        carrinho = CarrinhoDB.getInstancia(getApplicationContext()).buscarCarrinho();

        rvProdutosSupermercado = findViewById(R.id.rv_produto_supermercados);
        rvItensCarrinho = findViewById(R.id.rv_itens_carrinho);
        tvValorTotalCarrinho = findViewById(R.id.tv_valor_total_carrinho);

        DecimalFormat df = new DecimalFormat("#0.00");
        tvValorTotalCarrinho.setText("R$ " + (df.format(carrinho.getValorTotal()).replace(".", ",")));

        mAdapterItensCarrinho = new AdapterListaProdutosSupermercadoCarrinho(getApplicationContext(), carrinho.getProdutoSupermercadoCarrinhos());
        rvItensCarrinho.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rvItensCarrinho.setAdapter(mAdapterItensCarrinho);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
