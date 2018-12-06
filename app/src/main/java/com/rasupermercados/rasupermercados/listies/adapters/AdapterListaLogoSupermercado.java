package com.rasupermercados.rasupermercados.listies.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rasupermercados.rasupermercados.R;
import com.rasupermercados.rasupermercados.db.CarrinhoDB;
import com.rasupermercados.rasupermercados.fragments.BannerFragment;
import com.rasupermercados.rasupermercados.listies.viewholders.CategoriaViewHolder;
import com.rasupermercados.rasupermercados.listies.viewholders.LogoSupermercadoViewHolder;
import com.rasupermercados.rasupermercados.negocio.Carrinho;
import com.rasupermercados.rasupermercados.negocio.Categoria;
import com.rasupermercados.rasupermercados.negocio.ProdutoSupermercadoCarrinho;
import com.rasupermercados.rasupermercados.negocio.Supermercado;
import com.rasupermercados.rasupermercados.negocio.SupermercadoCarrinho;

import java.util.List;

public class AdapterListaLogoSupermercado extends RecyclerView.Adapter<LogoSupermercadoViewHolder> {
    private List<SupermercadoCarrinho> supermercados;
    private Context contexto;

    public AdapterListaLogoSupermercado(Context contexto, List<SupermercadoCarrinho> supermercados) {
        this.supermercados = supermercados;
        this.contexto = contexto;

    }

    public void atualizarLista(FragmentManager fragmentManager) {
        supermercados = CarrinhoDB.getInstancia(contexto).buscarSupermercadoCarrinho();

        Carrinho carrinho = CarrinhoDB.getInstancia(contexto).buscarCarrinho();
        if(carrinho.getProdutoSupermercadoCarrinhos() != null && carrinho.getProdutoSupermercadoCarrinhos().size() > 0)
            validarMostrarBanner(carrinho.getProdutoSupermercadoCarrinhos(), fragmentManager);

        notifyDataSetChanged();
    }

    private void validarMostrarBanner(List<ProdutoSupermercadoCarrinho> listaProdutosSupermercado, FragmentManager fragmentManager) {
        boolean mostrarBanner = true;
        int quantidadeProdutosTotal = 0;

        for(int i = 0; i<listaProdutosSupermercado.size();i++) {
            quantidadeProdutosTotal += listaProdutosSupermercado.get(i).getQuantidade();
            if(quantidadeProdutosTotal > 1) {
                mostrarBanner = false;
                break;
            }
        }

        if(mostrarBanner || (!mostrarBanner && supermercados.size() == 1)) {
            BannerFragment bannerFragment = new BannerFragment();
            Bundle extras = new Bundle();
            extras.putString("nome_imagem", "banner1.jpeg");
            bannerFragment.setArguments(extras);
            bannerFragment.show(fragmentManager,"frag_pagamento");
        }
    }

    @NonNull
    @Override
    public LogoSupermercadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_logo_supermercado, parent, false);

        LogoSupermercadoViewHolder lgoSupermercadoViewHolder = new LogoSupermercadoViewHolder(contexto, view);

        return lgoSupermercadoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LogoSupermercadoViewHolder holder, int position) {
        holder.setSupermercadoCarrinho(supermercados.get(position));
    }

    @Override
    public int getItemCount() {
        return supermercados.size();
    }
}
