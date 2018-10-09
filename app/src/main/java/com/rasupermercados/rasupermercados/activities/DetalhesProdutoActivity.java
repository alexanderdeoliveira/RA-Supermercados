package com.rasupermercados.rasupermercados.activities;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rasupermercados.rasupermercados.R;
import com.rasupermercados.rasupermercados.db.ProdutoDB;
import com.rasupermercados.rasupermercados.listies.adapters.AdapterListaProdutos;
import com.rasupermercados.rasupermercados.listies.adapters.AdapterListaProdutosSupermercado;
import com.rasupermercados.rasupermercados.negocio.Produto;
import com.rasupermercados.rasupermercados.negocio.ProdutoSupermercado;
import com.rasupermercados.rasupermercados.negocio.Supermercado;

import java.util.ArrayList;
import java.util.List;

public class DetalhesProdutoActivity extends AppCompatActivity implements ChildEventListener {

    private RecyclerView rvProdutosSupermercado;
    private List<ProdutoSupermercado> listaProdutosSupermercado = new ArrayList<>();
    private TextView tvNomeProduto;
    private AdapterListaProdutosSupermercado mAdapter;
    private ImageView ivImagemProduto;
    private Context contexto;
    private FirebaseDatabase database;
    private Produto produto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_produto);

        rvProdutosSupermercado = findViewById(R.id.rv_produto_supermercados);
        ivImagemProduto = findViewById(R.id.iv_produto);
        tvNomeProduto = findViewById(R.id.tv_nome_produto);
        contexto = this;

        StorageReference mStorageProdutos = FirebaseStorage.getInstance().getReference().child("produtos");

        int codigoProduto = getIntent().getIntExtra("codigoProduto", 0);
        produto = ProdutoDB.getInstancia(getApplicationContext()).buscarProduto(codigoProduto);
        tvNomeProduto.setText(produto.getNome());
        Glide.with(contexto)
                .using(new FirebaseImageLoader())
                .load(mStorageProdutos.child(produto.getUrlFotoStorage()))
                .into(ivImagemProduto);

        mAdapter = new AdapterListaProdutosSupermercado(getApplicationContext(), listaProdutosSupermercado);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvProdutosSupermercado.getContext(),
                layoutManager.getOrientation());
        rvProdutosSupermercado.addItemDecoration(dividerItemDecoration);
        rvProdutosSupermercado.setLayoutManager(layoutManager);
        rvProdutosSupermercado.setAdapter(mAdapter);

        database = FirebaseDatabase.getInstance();

        DatabaseReference refProdutos = database.getReference("produto_supermercado").child(Integer.toString(codigoProduto));

        refProdutos.addChildEventListener(this);

    }


    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        DatabaseReference refSupermercados = database.getReference("supermercados").child(dataSnapshot.getKey());

        final double valorProduto = Double.parseDouble(dataSnapshot.getValue().toString());
        ValueEventListener supermercadosListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Supermercado supermercado = dataSnapshot.getValue(Supermercado.class);

                ProdutoSupermercado produtoSupermercado = new ProdutoSupermercado();
                produtoSupermercado.setValor(valorProduto);
                produtoSupermercado.setSupermercado(supermercado);
                produtoSupermercado.setProduto(produto);

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

    }
}
