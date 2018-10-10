package com.rasupermercados.rasupermercados.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rasupermercados.rasupermercados.R;
import com.rasupermercados.rasupermercados.db.CategoriaDB;
import com.rasupermercados.rasupermercados.db.ProdutoDB;
import com.rasupermercados.rasupermercados.listies.adapters.AdapterListaCategoriasFiltros;
import com.rasupermercados.rasupermercados.negocio.Categoria;
import com.rasupermercados.rasupermercados.negocio.Produto;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CategoriasFiltroFragment extends DialogFragment implements ChildEventListener {

    private FirebaseDatabase database;
    private List<Categoria> categorias;
    private AdapterListaCategoriasFiltros adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categorias_filtro, container, false);

        RecyclerView rvCategorias = view.findViewById(R.id.rv_categorias);

        database = FirebaseDatabase.getInstance();

        DatabaseReference refCategorias = database.getReference("categorias");
        refCategorias.addChildEventListener(this);

        categorias = CategoriaDB.getInstancia(view.getContext()).buscarCategorias();

        adapter = new AdapterListaCategoriasFiltros(getActivity(), categorias);
        rvCategorias.setLayoutManager(new LinearLayoutManager(view.getContext() , LinearLayoutManager.VERTICAL, false));
        rvCategorias.setAdapter(adapter);

        return view;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Categoria categoria = dataSnapshot.getValue(Categoria.class);
        CategoriaDB categoriaDB = CategoriaDB.getInstancia(getApplicationContext());
        if(categoriaDB.atualizarCategoria(categoria) == 0) {
            categoriaDB.salvarCategoria(categoria);
            categorias.add(categoria);
            adapter.notifyItemInserted(Integer.parseInt(dataSnapshot.getKey()));

        }
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

    }
}
