package com.rasupermercados.rasupermercados.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.rasupermercados.rasupermercados.R;
import com.rasupermercados.rasupermercados.db.CarrinhoDB;
import com.rasupermercados.rasupermercados.fragments.BannerFragment;
import com.rasupermercados.rasupermercados.listies.adapters.AdapterListaProdutosSupermercadoCarrinho;
import com.rasupermercados.rasupermercados.negocio.Carrinho;
import com.rasupermercados.rasupermercados.negocio.ProdutoSupermercadoCarrinho;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class CarrinhoActivity extends AppCompatActivity {

    private RecyclerView rvItensCarrinho;
    private TextView tvValorTotalCarrinho;
    private AdapterListaProdutosSupermercadoCarrinho mAdapterItensCarrinho;
    private ItemTouchHelper itemTouchHelper;
    private CoordinatorLayout coordinatorLayout;
    private Button btFinalizarCompra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        coordinatorLayout = findViewById(R.id.layout_coodinator);

        Carrinho carrinho = CarrinhoDB.getInstancia(getApplicationContext()).buscarCarrinho();

        rvItensCarrinho = findViewById(R.id.rv_itens_carrinho);
        tvValorTotalCarrinho = findViewById(R.id.tv_valor_total_carrinho);
        btFinalizarCompra = findViewById(R.id.bt_finalizar_compra);
        btFinalizarCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarrinhoDB.getInstancia(getApplicationContext()).deletarCarrinho();
                setResult(2);
                finish();
            }
        });

        if(carrinho.getProdutoSupermercadoCarrinhos().size() > 0)
            btFinalizarCompra.setVisibility(View.VISIBLE);
        else
            btFinalizarCompra.setVisibility(View.GONE);

        tvValorTotalCarrinho.setText(carrinho.getValorTotal());

        mAdapterItensCarrinho = new AdapterListaProdutosSupermercadoCarrinho(getApplicationContext(), carrinho, tvValorTotalCarrinho, btFinalizarCompra);
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
                                btFinalizarCompra.setVisibility(View.VISIBLE);
                            }
                        });

                snackbar.setActionTextColor(Color.RED);

                snackbar.show();

                if(mAdapterItensCarrinho.carrinho.getProdutoSupermercadoCarrinhos().size() == 0)
                    btFinalizarCompra.setVisibility(View.GONE);
                else
                    btFinalizarCompra.setVisibility(View.VISIBLE);
            }

            public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,float dX, float dY,int actionState, boolean isCurrentlyActive){

                new RecyclerViewSwipeDecorator.Builder(getApplicationContext(), c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.background_color_vermelho))
                        .addActionIcon(R.drawable.ic_delete_white_24dp)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
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
