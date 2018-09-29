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
import com.rasupermercados.rasupermercados.listies.adapters.AdapterListaProdutos;
import com.rasupermercados.rasupermercados.listies.adapters.AdapterListaProdutosSupermercado;
import com.rasupermercados.rasupermercados.negocio.Produto;
import com.rasupermercados.rasupermercados.negocio.ProdutoSupermercado;
import com.rasupermercados.rasupermercados.negocio.Supermercado;

import java.util.ArrayList;
import java.util.List;

public class DetalhesProdutoActivity extends AppCompatActivity {

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

        int codigoProduto = getIntent().getIntExtra("codigoProduto", 0);

        mAdapter = new AdapterListaProdutosSupermercado(getApplicationContext(), listaProdutosSupermercado);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvProdutosSupermercado.getContext(),
                layoutManager.getOrientation());
        rvProdutosSupermercado.addItemDecoration(dividerItemDecoration);
        rvProdutosSupermercado.setLayoutManager(layoutManager);
        rvProdutosSupermercado.setAdapter(mAdapter);

        database = FirebaseDatabase.getInstance();
        DatabaseReference produtoDataBaseRef = database.getReference("produtos").child(Integer.toString(codigoProduto));

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Produto produto = dataSnapshot.getValue(Produto.class);
                tvNomeProduto.setText(produto.getNome());

                StorageReference produtoStorageRef = FirebaseStorage.getInstance().getReference().child("produtos").child(produto.getUrlFotoStorage());

                final long ONE_MEGABYTE = 1024 * 1024;
                produtoStorageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Drawable imagemProduto = new BitmapDrawable(contexto.getResources(), BitmapFactory.decodeByteArray(bytes, 0, bytes.length));

                        ivImagemProduto.setImageDrawable(imagemProduto);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        produtoDataBaseRef.addListenerForSingleValueEvent(postListener);


        DatabaseReference refProdutos = database.getReference("produto_supermercado").child(Integer.toString(codigoProduto));

        ChildEventListener childProdutosListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("RA SUPERMERCADOS", "add:" + dataSnapshot.getKey() + " " + dataSnapshot.getValue());
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
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("RA SUPERMERCADOS", "onChildChanged:" + dataSnapshot.getKey());

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("RA SUPERMERCADOS", "onChildRemoved:" + dataSnapshot.getKey());


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("RA SUPERMERCADOS", "onChildMoved:" + dataSnapshot.getKey());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("RA SUPERMERCADOS", "postComments:onCancelled", databaseError.toException());
                Toast.makeText(getApplicationContext(), "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        };

        refProdutos.addChildEventListener(childProdutosListener);


    }


}
