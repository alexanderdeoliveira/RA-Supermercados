package com.rasupermercados.rasupermercados.listies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.storage.StorageReference;
import com.rasupermercados.rasupermercados.R;
import com.rasupermercados.rasupermercados.db.CarrinhoDB;
import com.rasupermercados.rasupermercados.listies.viewholders.ProdutoSupermercadoCarrinhoViewHolder;
import com.rasupermercados.rasupermercados.listies.viewholders.ProdutoSupermercadoViewHolder;
import com.rasupermercados.rasupermercados.negocio.Carrinho;
import com.rasupermercados.rasupermercados.negocio.ProdutoSupermercado;
import com.rasupermercados.rasupermercados.negocio.ProdutoSupermercadoCarrinho;

import java.text.DecimalFormat;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AdapterListaProdutosSupermercadoCarrinho extends RecyclerView.Adapter<ProdutoSupermercadoCarrinhoViewHolder> {
    public Carrinho carrinho;
    private Context contexto;
    private TextView tvValorTotal;
    private Button btFinalizarCompra;

    public AdapterListaProdutosSupermercadoCarrinho(Context contexto, Carrinho carrinho, TextView tvValorTotal, Button btFinalizar) {
        this.carrinho = carrinho;
        this.contexto = contexto;
        this.tvValorTotal = tvValorTotal;
        this.btFinalizarCompra = btFinalizar;
    }
    @NonNull
    @Override
    public ProdutoSupermercadoCarrinhoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_produto_supermercado_carrinho, parent, false);

        ProdutoSupermercadoCarrinhoViewHolder produtoViewHolder = new ProdutoSupermercadoCarrinhoViewHolder(contexto, view, this);

        return produtoViewHolder;
    }

    public void atualizarCarrinho() {
        carrinho = CarrinhoDB.getInstancia(contexto).buscarCarrinho();
    }

    public void addItem(ProdutoSupermercadoCarrinho produtoSupermercadoCarrinho) {
        this.carrinho.addProdutoSupermercadoCarrinho(produtoSupermercadoCarrinho);
        notifyItemInserted(getItemCount());

        tvValorTotal.setText(this.carrinho.getValorTotal());

        CarrinhoDB.getInstancia(contexto).salvarProdutoSupermercadoCarrinho(produtoSupermercadoCarrinho);
    }

    public void addItem(ProdutoSupermercadoCarrinho produtoSupermercadoCarrinho, int position) {
        this.carrinho.addProdutoSupermercadoCarrinho(produtoSupermercadoCarrinho, position);
        notifyItemInserted(position);

        tvValorTotal.setText(this.carrinho.getValorTotal());

        CarrinhoDB.getInstancia(contexto).salvarProdutoSupermercadoCarrinho(produtoSupermercadoCarrinho);
    }

    public void alterarItem(int position, ProdutoSupermercadoCarrinho produtoSupermercadoCarrinhoAlterado) {
        this.carrinho.alterarProdutoSupermercadoCarrinho(position, produtoSupermercadoCarrinhoAlterado);
        tvValorTotal.setText(this.carrinho.getValorTotal());
        notifyItemChanged(position);

        CarrinhoDB.getInstancia(contexto).atualizarQuantidadeProdutoCarrinho(produtoSupermercadoCarrinhoAlterado);
    }

    public void removeItem(int position, ProdutoSupermercadoCarrinho produtoSupermercadoCarrinhoExcluido) {
        CarrinhoDB.getInstancia(contexto).deletarProdutoCarrinho(produtoSupermercadoCarrinhoExcluido);
        this.carrinho.removeProdutoSupermercadoCarrinho(position);
        notifyItemRemoved(position);

        tvValorTotal.setText(this.carrinho.getValorTotal());

        if(btFinalizarCompra != null) {
            if (carrinho.getProdutoSupermercadoCarrinhos().size() == 0)
                btFinalizarCompra.setVisibility(View.GONE);
            else
                btFinalizarCompra.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoSupermercadoCarrinhoViewHolder holder, int position) {
        holder.setItem(this.carrinho.getProdutoSupermercadoCarrinhos().get(position), position);
    }

    @Override
    public int getItemCount() {
        return this.carrinho.getProdutoSupermercadoCarrinhos().size();
    }
}
