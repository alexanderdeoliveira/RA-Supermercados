package com.rasupermercados.rasupermercados.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rasupermercados.rasupermercados.R;
import com.rasupermercados.rasupermercados.db.CarrinhoDB;
import com.rasupermercados.rasupermercados.db.ProdutoDB;
import com.rasupermercados.rasupermercados.fragments.BannerFragment;
import com.rasupermercados.rasupermercados.fragments.ConfirmarProdutoDialog;
import com.rasupermercados.rasupermercados.listies.adapters.AdapterListaLogoSupermercado;
import com.rasupermercados.rasupermercados.listies.adapters.AdapterListaProdutosSupermercado;
import com.rasupermercados.rasupermercados.listies.adapters.AdapterListaProdutosSupermercadoCarrinho;
import com.rasupermercados.rasupermercados.negocio.Carrinho;
import com.rasupermercados.rasupermercados.negocio.Produto;
import com.rasupermercados.rasupermercados.negocio.ProdutoSupermercadoCarrinho;
import com.rasupermercados.rasupermercados.negocio.SupermercadoCarrinho;
import com.rasupermercados.rasupermercados.utils.CustomBottomSheetBehavior;
import java.text.DecimalFormat;;

public class DetalhesProdutoActivity extends AppCompatActivity implements ConfirmarProdutoDialog.OnAdicionarAoCarrinhoListener {

    private RecyclerView rvProdutosSupermercado;
    private TextView tvNomeProduto;
    private AdapterListaProdutosSupermercadoCarrinho mAdapterItensCarrinho;
    private ImageView ivImagemProduto;
    private Context contexto;

    private Produto produto;
    private CustomBottomSheetBehavior bottomSheetBehavior;
    private ImageView ivCarrinho;
    //private RecyclerView rvItensCarrinho;
    private RecyclerView rvLogos;
    private TextView tvValorTotalCarrinho;
    private AdapterListaLogoSupermercado mAdapterLogos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_produto_2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Carrinho carrinho = CarrinhoDB.getInstancia(getApplicationContext()).buscarCarrinho();

        rvProdutosSupermercado = findViewById(R.id.rv_produto_supermercados);
        //rvItensCarrinho = findViewById(R.id.rv_itens_carrinho);

        tvValorTotalCarrinho = findViewById(R.id.tv_valor_total_carrinho);
        tvValorTotalCarrinho.setText(carrinho.getValorTotal());

        ivImagemProduto = findViewById(R.id.iv_produto);
        tvNomeProduto = findViewById(R.id.tv_nome_produto);
        ivCarrinho = findViewById(R.id.iv_carrinho);

        final LinearLayout bottomSheetLayout = findViewById(R.id.layout_bottom_sheet);

        bottomSheetBehavior = CustomBottomSheetBehavior.from(bottomSheetLayout);
        bottomSheetBehavior.setState(CustomBottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setHideable(true);
        //bottomSheetBehavior.setState(CustomBottomSheetBehavior.STATE_EXPANDED);

        ivCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState() == CustomBottomSheetBehavior.STATE_COLLAPSED || bottomSheetBehavior.getState() == CustomBottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(CustomBottomSheetBehavior.STATE_HIDDEN);
                }
                else {
                    bottomSheetBehavior.setState(CustomBottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        contexto = this;

        StorageReference mStorageProdutos = FirebaseStorage.getInstance().getReference().child("produtos");

        int codigoProduto = getIntent().getIntExtra("codigoProduto", 0);
        produto = ProdutoDB.getInstancia(getApplicationContext()).buscarProduto(codigoProduto);
        tvNomeProduto.setText(produto.getNome());
        Glide.with(contexto)
                .using(new FirebaseImageLoader())
                .load(mStorageProdutos.child(produto.getUrlFotoStorage()))
                .into(ivImagemProduto);

        AdapterListaProdutosSupermercado mAdapterProdutosSupermercado = new AdapterListaProdutosSupermercado(getApplicationContext(), produto, getSupportFragmentManager());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvProdutosSupermercado.getContext(),
                layoutManager.getOrientation());
        rvProdutosSupermercado.addItemDecoration(dividerItemDecoration);
        rvProdutosSupermercado.setLayoutManager(layoutManager);
        rvProdutosSupermercado.setAdapter(mAdapterProdutosSupermercado);

        rvLogos = findViewById(R.id.rv_logos_supermercados);
        mAdapterLogos = new AdapterListaLogoSupermercado(getApplicationContext(), CarrinhoDB.getInstancia(getApplicationContext()).buscarSupermercadoCarrinho());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvLogos.setLayoutManager(gridLayoutManager);
        rvLogos.setAdapter(mAdapterLogos);

        mAdapterItensCarrinho = new AdapterListaProdutosSupermercadoCarrinho(getApplicationContext(), carrinho, tvValorTotalCarrinho, null);
        /*rvItensCarrinho.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rvItensCarrinho.setAdapter(mAdapterItensCarrinho);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_carrinho, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.abrir_carrinho:
                startActivityForResult(new Intent(this, CarrinhoActivity.class), 1);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            mAdapterLogos.atualizarLista(getSupportFragmentManager());
            mAdapterItensCarrinho.atualizarCarrinho();
            tvValorTotalCarrinho.setText(mAdapterItensCarrinho.carrinho.getValorTotal());
            bottomSheetBehavior.setState(CustomBottomSheetBehavior.STATE_EXPANDED);

            if (resultCode == 2) {
                BannerFragment bannerFragment = new BannerFragment();
                Bundle extras = new Bundle();
                extras.putString("nome_imagem", "banner2.jpeg");
                bannerFragment.setArguments(extras);
                bannerFragment.show(getSupportFragmentManager(), "frag_pagamento");
            }
        }
    }

    @Override
    public void onAdicionarAoCarrinho(ProdutoSupermercadoCarrinho produtoSupermercadoCarrinho) {
        mAdapterItensCarrinho.addItem(produtoSupermercadoCarrinho);
        mAdapterLogos.atualizarLista(getSupportFragmentManager());

        tvValorTotalCarrinho.setText(mAdapterItensCarrinho.carrinho.getValorTotal());
        bottomSheetBehavior.setState(CustomBottomSheetBehavior.STATE_EXPANDED);
    }
}
