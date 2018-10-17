package com.rasupermercados.rasupermercados.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rasupermercados.rasupermercados.R;
import com.rasupermercados.rasupermercados.db.CarrinhoDB;
import com.rasupermercados.rasupermercados.db.ProdutoDB;
import com.rasupermercados.rasupermercados.fragments.ConfirmarProdutoDialog;
import com.rasupermercados.rasupermercados.listies.adapters.AdapterListaProdutosSupermercado;
import com.rasupermercados.rasupermercados.listies.adapters.AdapterListaProdutosSupermercadoCarrinho;
import com.rasupermercados.rasupermercados.negocio.Carrinho;
import com.rasupermercados.rasupermercados.negocio.Produto;
import com.rasupermercados.rasupermercados.negocio.ProdutoSupermercado;
import com.rasupermercados.rasupermercados.negocio.ProdutoSupermercadoCarrinho;
import com.rasupermercados.rasupermercados.negocio.Supermercado;
import com.rasupermercados.rasupermercados.utils.CustomBottomSheetBehavior;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DetalhesProdutoActivity extends AppCompatActivity implements ConfirmarProdutoDialog.OnAdicionarAoCarrinhoListener {

    private RecyclerView rvProdutosSupermercado;
    private TextView tvNomeProduto;
    private AdapterListaProdutosSupermercadoCarrinho mAdapterItensCarrinho;
    private ImageView ivImagemProduto;
    private Context contexto;

    private Produto produto;
    private CustomBottomSheetBehavior bottomSheetBehavior;
    private ImageView ivCarrinho;
    private RecyclerView rvItensCarrinho;
    private TextView tvValorTotalCarrinho;
    private Carrinho carrinho;

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

        ivImagemProduto = findViewById(R.id.iv_produto);
        tvNomeProduto = findViewById(R.id.tv_nome_produto);
        ivCarrinho = findViewById(R.id.iv_carrinho);

        final LinearLayout bottomSheetLayout = findViewById(R.id.layout_bottom_sheet);

        /*mAdapterProdutPromocao = new AdapterListaLogoSupermercado(getApplicationContext(), produtosPromocao);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvPromocoes.setLayoutManager(layoutManager);
        rvPromocoes.setAdapter(mAdapterProdutPromocao);*/

        bottomSheetBehavior = CustomBottomSheetBehavior.from(bottomSheetLayout);
        bottomSheetBehavior.setState(CustomBottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setHideable(true);

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

        mAdapterItensCarrinho = new AdapterListaProdutosSupermercadoCarrinho(getApplicationContext(), carrinho.getProdutoSupermercadoCarrinhos());
        rvItensCarrinho.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rvItensCarrinho.setAdapter(mAdapterItensCarrinho);
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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAdicionarAoCarrinho(ProdutoSupermercadoCarrinho produtoSupermercadoCarrinho) {
        //mAdapterItensCarrinho.addItem(produtoSupermercadoCarrinho);
        carrinho.addProdutoSupermercadoCarrinho(produtoSupermercadoCarrinho);
        mAdapterItensCarrinho.notifyItemInserted(carrinho.getProdutoSupermercadoCarrinhos().size());

        DecimalFormat df = new DecimalFormat("#0.00");
        tvValorTotalCarrinho.setText("R$ " + (df.format(carrinho.getValorTotal()).replace(".", ",")));
        bottomSheetBehavior.setState(CustomBottomSheetBehavior.STATE_EXPANDED);
    }


    /*@Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        DatabaseReference refSupermercados = database.getReference("supermercados").child(dataSnapshot.getKey());

        final double valorProduto = Double.parseDouble(dataSnapshot.getValue().toString());
        ValueEventListener supermercadosListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Supermercado supermercado = dataSnapshot.getValue(Supermercado.class);

                ProdutoSupermercado produtoSupermercado = new ProdutoSupermercado(supermercado, valorProduto);
                mAdapter.addItem(produtoSupermercado);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        refSupermercados.addListenerForSingleValueEvent(supermercadosListener);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        String i = "";

    }*/
}
