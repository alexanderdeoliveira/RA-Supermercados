package com.rasupermercados.rasupermercados.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rasupermercados.rasupermercados.R;
import com.rasupermercados.rasupermercados.db.CategoriaDB;
import com.rasupermercados.rasupermercados.db.ProdutoDB;
import com.rasupermercados.rasupermercados.db.UsuarioDB;
import com.rasupermercados.rasupermercados.listies.adapters.AdapterListaCategorias;
import com.rasupermercados.rasupermercados.negocio.Categoria;
import com.rasupermercados.rasupermercados.negocio.Produto;
import com.rasupermercados.rasupermercados.negocio.Usuario;

import java.util.List;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AdapterListaCategorias mAdapterCategorias;
    private List<Categoria> categorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView tvNomeUsuario = headerView.findViewById(R.id.tv_nome_usuario);
        TextView tvEmail = headerView.findViewById(R.id.tv_email);

        Usuario usuario = UsuarioDB.getInstancia(getApplicationContext()).buscarUsuario();
        tvNomeUsuario.setText(usuario.getNome());
        tvEmail.setText(usuario.getEmail());

        categorias = CategoriaDB.getInstancia(getApplicationContext()).buscarCategorias();

        RecyclerView rvCategorias = findViewById(R.id.rv_categorias);
        mAdapterCategorias = new AdapterListaCategorias(getApplicationContext(), categorias);
        rvCategorias.setLayoutManager(new GridLayoutManager(this, 2));
        rvCategorias.setAdapter(mAdapterCategorias);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refProdutos = database.getReference("categorias");

        ChildEventListener childProdutosListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("RA SUPERMERCADOS", "onChildAdded:" + dataSnapshot.getKey());

                Categoria categoria = dataSnapshot.getValue(Categoria.class);
                categorias.add(categoria);

                CategoriaDB categoriaDB = CategoriaDB.getInstancia(getApplicationContext());
                categoriaDB.salvarCategoria(categoria);

                mAdapterCategorias.notifyItemInserted(Integer.parseInt(dataSnapshot.getKey()));

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Categoria categoria = dataSnapshot.getValue(Categoria.class);
                categorias.get(Integer.parseInt(dataSnapshot.getKey())).setNome(categoria.getNome());
                categorias.get(Integer.parseInt(dataSnapshot.getKey())).setCodigoCategoria(categoria.getCodigoCategoria());
                categorias.get(Integer.parseInt(dataSnapshot.getKey())).setUrlFotoCategoria(categoria.getUrlFotoCategoria());

                mAdapterCategorias.notifyItemChanged(Integer.parseInt(dataSnapshot.getKey()));

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Categoria categoria = dataSnapshot.getValue(Categoria.class);
                categorias.remove(categoria);

                CategoriaDB categoriaDB = CategoriaDB.getInstancia(getApplicationContext());
                categoriaDB.deletarCategoria(categoria.getCodigoCategoria());

                mAdapterCategorias.notifyItemRemoved(Integer.parseInt(dataSnapshot.getKey()));
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
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
