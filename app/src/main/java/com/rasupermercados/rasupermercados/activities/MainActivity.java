package com.rasupermercados.rasupermercados.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rasupermercados.rasupermercados.R;
import com.rasupermercados.rasupermercados.db.ProdutoDB;
import com.rasupermercados.rasupermercados.db.UsuarioDB;
import com.rasupermercados.rasupermercados.fragments.CategoriasFiltroFragment;
import com.rasupermercados.rasupermercados.fragments.BannerFragment;
import com.rasupermercados.rasupermercados.listies.adapters.AdapterListaFiltrosAplicados;
import com.rasupermercados.rasupermercados.listies.adapters.AdapterListaProdutos;
import com.rasupermercados.rasupermercados.listies.adapters.AdapterListaProdutosPromocao;
import com.rasupermercados.rasupermercados.negocio.Categoria;
import com.rasupermercados.rasupermercados.negocio.Produto;
import com.rasupermercados.rasupermercados.negocio.ProdutoFirebase;
import com.rasupermercados.rasupermercados.negocio.Usuario;
import com.rasupermercados.rasupermercados.utils.Constantes;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener, ChildEventListener, CategoriasFiltroFragment.onAplicarFiltrosListener {

    private List<Produto> produtos;
    private AdapterListaProdutos mAdapterProduto;
    private SearchView searchView;
    private LinearLayout layoutCarregando;
    private RecyclerView rvProdutos;
    private FirebaseDatabase database;
    private DatabaseReference refProdutos;

    private List<Produto> produtosPromocao;
    private RecyclerView rvPromocoes;
    private AdapterListaProdutosPromocao mAdapterProdutPromocao;
    private LinearLayout layoutPromocoes;
    private RecyclerView rvFiltros;
    private AdapterListaFiltrosAplicados mAdapterFiltrosAplicados;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab_filtrar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategoriasFiltroFragment categoriasFiltroFragment = new CategoriasFiltroFragment();
                categoriasFiltroFragment.show(getSupportFragmentManager(),"frag_filtros");
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView tvNomeUsuario = headerView.findViewById(R.id.tv_nome_usuario);
        TextView tvEmail = headerView.findViewById(R.id.tv_email);

        Usuario usuario = UsuarioDB.getInstancia(getApplicationContext()).buscarUsuario();
        tvNomeUsuario.setText(usuario.getNome());
        tvEmail.setText(usuario.getEmail());

        database = FirebaseDatabase.getInstance();
        refProdutos = database.getReference("produtos");

        rvProdutos = findViewById(R.id.rv_produtos);
        layoutCarregando = findViewById(R.id.layout_carregando);
        layoutPromocoes = findViewById(R.id.layout_promocao);
        rvPromocoes = findViewById(R.id.rv_promocoes);

        produtos = ProdutoDB.getInstancia(getApplicationContext()).buscarProdutos();
        mAdapterProduto = new AdapterListaProdutos(getApplicationContext(), produtos);

        rvProdutos.setLayoutManager(new GridLayoutManager(this, 2));
        rvProdutos.setAdapter(mAdapterProduto);

        produtosPromocao = ProdutoDB.getInstancia(getApplicationContext()).buscarProdutos("CO");
        mAdapterProdutPromocao = new AdapterListaProdutosPromocao(getApplicationContext(), produtosPromocao);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvPromocoes.setLayoutManager(layoutManager);
        rvPromocoes.setAdapter(mAdapterProdutPromocao);

        layoutCarregando.setVisibility(View.GONE);
        rvProdutos.setVisibility(View.VISIBLE);

        refProdutos.addChildEventListener(this);

        rvFiltros = findViewById(R.id.rv_filtros);
        rvFiltros.setLayoutManager(layoutManager);
        mAdapterFiltrosAplicados = new AdapterListaFiltrosAplicados(this, new ArrayList<Categoria>());
        rvFiltros.setAdapter(mAdapterFiltrosAplicados);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setQueryHint("Ex.:Arroz, Feijao...");

        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_logout:
                fazerLogout();
                break;

            case R.id.nav_pagamento:
                BannerFragment bannerFragment = new BannerFragment();
                Bundle extras = new Bundle();
                extras.putString("nome_imagem", "banner_pagamento.jpeg");
                bannerFragment.setArguments(extras);
                bannerFragment.show(getSupportFragmentManager(),"frag_pagamento");
                break;

            case R.id.nav_carrinho:
                startActivityForResult(new Intent(this, CarrinhoActivity.class), 1);
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1) {
            if(resultCode == 2) {
                BannerFragment bannerFragment = new BannerFragment();
                Bundle extras = new Bundle();
                extras.putString("nome_imagem", "banner2.jpeg");
                bannerFragment.setArguments(extras);
                bannerFragment.show(getSupportFragmentManager(),"frag_pagamento");
            }
        }
    }

    private void fazerLogout() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        UsuarioDB.getInstancia(getApplicationContext()).deletarUsuario();
        setResult(Constantes.RESULT_REQUEST_LOGOUT);
        finish();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        layoutPromocoes.setVisibility(View.GONE);
        produtos = ProdutoDB.getInstancia(getApplicationContext()).buscarProdutos(query);
        mAdapterProduto.atualizarListaProdutos(produtos);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(newText.equals("")) {
            produtos = ProdutoDB.getInstancia(getApplicationContext()).buscarProdutos();
            mAdapterProduto.atualizarListaProdutos(produtos);

            layoutPromocoes.setVisibility(View.VISIBLE);
        }
        return false;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
        ProdutoFirebase produtoFirebase = dataSnapshot.getValue(ProdutoFirebase.class);
        Produto produto = new Produto(produtoFirebase);
        ProdutoDB produtoDB = ProdutoDB.getInstancia(getApplicationContext());
        if(produtoDB.atualizarProduto(produto) == 0) {
            produtoDB.salvarProduto(produto);
            produtos.add(produto);
            mAdapterProduto.notifyItemInserted(Integer.parseInt(dataSnapshot.getKey()));

            Log.d("RA SUPERMERCADOS", "onChildAdded:" + dataSnapshot.getKey());
        }

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
        ProdutoFirebase produtoFirebase = dataSnapshot.getValue(ProdutoFirebase.class);
        Produto prodrutoAlterado = new Produto(produtoFirebase);
        produtos.get(Integer.parseInt(dataSnapshot.getKey())).setNome(prodrutoAlterado.getNome());
        produtos.get(Integer.parseInt(dataSnapshot.getKey())).setCodigo(prodrutoAlterado.getCodigo());
        produtos.get(Integer.parseInt(dataSnapshot.getKey())).setUrlFotoStorage(prodrutoAlterado.getUrlFotoStorage());
        produtos.get(Integer.parseInt(dataSnapshot.getKey())).setCategoria(prodrutoAlterado.getCategoria());

        mAdapterProduto.notifyItemChanged(Integer.parseInt(dataSnapshot.getKey()));

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        ProdutoFirebase produtoFirebase = dataSnapshot.getValue(ProdutoFirebase.class);
        Produto produto = new Produto(produtoFirebase);
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

    @Override
    public void onAplicarFiltros(List<Categoria> filtros) {
        layoutPromocoes.setVisibility(View.GONE);
        produtos = ProdutoDB.getInstancia(getApplicationContext()).buscarProdutosPorCategoria(filtros);
        mAdapterProduto.atualizarListaProdutos(produtos);

        String texto = "";
        for(int i = 0;i<filtros.size();i++) {
            texto += filtros.get(i).getNome() + " ";
            mAdapterFiltrosAplicados.addItem(filtros.get(i));
        }
        rvFiltros.setVisibility(View.VISIBLE);
        searchView.setQuery(texto, false);
    }
}