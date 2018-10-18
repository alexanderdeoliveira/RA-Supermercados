package com.rasupermercados.rasupermercados.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.rasupermercados.rasupermercados.R;
import com.rasupermercados.rasupermercados.db.CarrinhoDB;
import com.rasupermercados.rasupermercados.listies.adapters.AdapterListaProdutosSupermercadoCarrinho;
import com.rasupermercados.rasupermercados.negocio.Carrinho;
import com.rasupermercados.rasupermercados.negocio.ProdutoSupermercadoCarrinho;

public class CarrinhoActivity extends AppCompatActivity {

    private RecyclerView rvItensCarrinho;
    private TextView tvValorTotalCarrinho;
    private AdapterListaProdutosSupermercadoCarrinho mAdapterItensCarrinho;
    private ItemTouchHelper itemTouchHelper;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        coordinatorLayout = findViewById(R.id.layout_coodinator);

        Carrinho carrinho = CarrinhoDB.getInstancia(getApplicationContext()).buscarCarrinho();

        rvItensCarrinho = findViewById(R.id.rv_itens_carrinho);
        tvValorTotalCarrinho = findViewById(R.id.tv_valor_total_carrinho);

        tvValorTotalCarrinho.setText(carrinho.getValorTotal());

        mAdapterItensCarrinho = new AdapterListaProdutosSupermercadoCarrinho(getApplicationContext(), carrinho, tvValorTotalCarrinho);
        rvItensCarrinho.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rvItensCarrinho.setAdapter(mAdapterItensCarrinho);

        itemTouchHelper = new ItemTouchHelper(createHelperCallBack());
        itemTouchHelper.attachToRecyclerView(rvItensCarrinho);
    }

    private ItemTouchHelper.Callback createHelperCallBack() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final ProdutoSupermercadoCarrinho itemRemovido = mAdapterItensCarrinho.carrinho.getProdutoSupermercadoCarrinhos().get(viewHolder.getAdapterPosition());
                final int positionItemRemovido = viewHolder.getAdapterPosition();
                mAdapterItensCarrinho.removeItem(viewHolder.getAdapterPosition(), itemRemovido);
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "O produto foi exluído do carrinho", Snackbar.LENGTH_LONG)
                        .setAction("Desfazer", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mAdapterItensCarrinho.addItem(itemRemovido, positionItemRemovido);
                            }
                        });

                snackbar.setActionTextColor(Color.RED);

                snackbar.show();
            }
        };

        return simpleCallback;
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
