package com.rasupermercados.rasupermercados.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rasupermercados.rasupermercados.R;
import com.rasupermercados.rasupermercados.db.CategoriaDB;
import com.rasupermercados.rasupermercados.listies.adapters.AdapterListaCategoriasFiltros;
import com.rasupermercados.rasupermercados.negocio.Categoria;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class PagamentoFragment extends DialogFragment {

    private ImageView ivBanner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pagamento, container, false);

        return view;
    }

}
