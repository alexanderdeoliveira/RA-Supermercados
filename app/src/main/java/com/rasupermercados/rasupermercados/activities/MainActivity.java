package com.rasupermercados.rasupermercados.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rasupermercados.rasupermercados.R;
import com.rasupermercados.rasupermercados.listies.adapters.AdapterListaProdutos;
import com.rasupermercados.rasupermercados.negocio.Produto;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Produto> prodrutos;
    private AdapterListaProdutos mAdapterProduto;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        RecyclerView rvProdutos = findViewById(R.id.rvProdutos);

        prodrutos = new ArrayList<Produto>();
        mAdapterProduto = new AdapterListaProdutos(getApplicationContext(), prodrutos);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvProdutos.getContext(),
                layoutManager.getOrientation());
        rvProdutos.addItemDecoration(dividerItemDecoration);
        rvProdutos.setLayoutManager(layoutManager);
        rvProdutos.setAdapter(mAdapterProduto);



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refProdutos = database.getReference("produtos");


        ChildEventListener childProdutosListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("RA SUPERMERCADOS", "onChildAdded:" + dataSnapshot.getKey());

                Produto prodruto = dataSnapshot.getValue(Produto.class);
                prodrutos.add(prodruto);

                mAdapterProduto.notifyItemInserted(Integer.parseInt(dataSnapshot.getKey()));

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("RA SUPERMERCADOS", "onChildChanged:" + dataSnapshot.getKey());

                Produto prodrutoAlterado = dataSnapshot.getValue(Produto.class);
                prodrutos.get(Integer.parseInt(dataSnapshot.getKey())).setNome(prodrutoAlterado.getNome());
                prodrutos.get(Integer.parseInt(dataSnapshot.getKey())).setCodigo(prodrutoAlterado.getCodigo());
                prodrutos.get(Integer.parseInt(dataSnapshot.getKey())).setUrlFotoStorage(prodrutoAlterado.getUrlFotoStorage());

                mAdapterProduto.notifyItemChanged(Integer.parseInt(dataSnapshot.getKey()));

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("RA SUPERMERCADOS", "onChildRemoved:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                Produto produto = dataSnapshot.getValue(Produto.class);
                prodrutos.remove(produto);

                mAdapterProduto.notifyItemRemoved(Integer.parseInt(dataSnapshot.getKey()));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("RA SUPERMERCADOS", "onChildMoved:" + dataSnapshot.getKey());

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                Produto movedComment = dataSnapshot.getValue(Produto.class);
                String commentKey = dataSnapshot.getKey();

                // ...
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
