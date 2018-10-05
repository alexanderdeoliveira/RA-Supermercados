package com.rasupermercados.rasupermercados.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rasupermercados.rasupermercados.R;
import com.rasupermercados.rasupermercados.db.ProdutoDB;
import com.rasupermercados.rasupermercados.listies.adapters.AdapterListaProdutos;
import com.rasupermercados.rasupermercados.negocio.Produto;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Produto> produtos;
    private AdapterListaProdutos mAdapterProduto;
    private SearchView searchView;

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
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            List<Produto> produtos = ProdutoDB.getInstancia(getApplicationContext()).buscarProduto(query);
            int i = 0;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handleIntent(getIntent());

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        RecyclerView rvProdutos = findViewById(R.id.rvProdutos);

        produtos = new ArrayList<>();
        mAdapterProduto = new AdapterListaProdutos(getApplicationContext(), produtos);
       /* LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvProdutos.getContext(),
                layoutManager.getOrientation());
        rvProdutos.addItemDecoration(dividerItemDecoration);*/
        rvProdutos.setLayoutManager(new GridLayoutManager(this, 2));
        rvProdutos.setAdapter(mAdapterProduto);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refProdutos = database.getReference("produtos");

        ChildEventListener childProdutosListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("RA SUPERMERCADOS", "onChildAdded:" + dataSnapshot.getKey());

                Produto produto = dataSnapshot.getValue(Produto.class);
                produtos.add(produto);

                ProdutoDB produtoDB = ProdutoDB.getInstancia(getApplicationContext());
                produtoDB.salvarProduto(produto);

                mAdapterProduto.notifyItemInserted(Integer.parseInt(dataSnapshot.getKey()));

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Produto prodrutoAlterado = dataSnapshot.getValue(Produto.class);
                produtos.get(Integer.parseInt(dataSnapshot.getKey())).setNome(prodrutoAlterado.getNome());
                produtos.get(Integer.parseInt(dataSnapshot.getKey())).setCodigo(prodrutoAlterado.getCodigo());
                produtos.get(Integer.parseInt(dataSnapshot.getKey())).setUrlFotoStorage(prodrutoAlterado.getUrlFotoStorage());

                mAdapterProduto.notifyItemChanged(Integer.parseInt(dataSnapshot.getKey()));

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Produto produto = dataSnapshot.getValue(Produto.class);
                produtos.remove(produto);

                ProdutoDB produtoDB = ProdutoDB.getInstancia(getApplicationContext());
                produtoDB.deletarProduto(produto.getCodigo());

                mAdapterProduto.notifyItemRemoved(Integer.parseInt(dataSnapshot.getKey()));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("RA SUPERMERCADOS", "onChildMoved:" + dataSnapshot.getKey());

                Produto movedComment = dataSnapshot.getValue(Produto.class);
                String commentKey = dataSnapshot.getKey();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        final SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

}
