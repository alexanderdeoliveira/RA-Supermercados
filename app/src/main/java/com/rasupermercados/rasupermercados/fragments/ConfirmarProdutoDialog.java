package com.rasupermercados.rasupermercados.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.rasupermercados.rasupermercados.R;
import com.rasupermercados.rasupermercados.db.CarrinhoDB;
import com.rasupermercados.rasupermercados.db.ProdutoDB;
import com.rasupermercados.rasupermercados.negocio.Carrinho;
import com.rasupermercados.rasupermercados.negocio.Produto;
import com.rasupermercados.rasupermercados.negocio.ProdutoSupermercado;
import com.rasupermercados.rasupermercados.negocio.ProdutoSupermercadoCarrinho;
import com.rasupermercados.rasupermercados.negocio.Supermercado;

import java.text.DecimalFormat;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ConfirmarProdutoDialog extends DialogFragment {
    private Button btAdicionarAoCarrinho;
    private TextView tvQtde, tvNomeProduto, tvNomeSupermercado, tvValorProduto, tvValorTotal;
    private OnAdicionarAoCarrinhoListener mAdicionarAoCarrinho;
    private Produto produto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirmar_produto, container, false);

        tvNomeProduto = view.findViewById(R.id.tv_nome_produto);
        tvQtde = view.findViewById(R.id.tv_qtd_produto);
        tvNomeSupermercado = view.findViewById(R.id.tv_nome_supermercado);
        tvValorProduto = view.findViewById(R.id.tv_valor_produto);
        tvValorTotal = view.findViewById(R.id.tv_valor_total);

        int codigoProduto = getArguments().getInt("codigoProduto");
        final int quantidade = getArguments().getInt("quantidade");
        final int codigoSupermercado = getArguments().getInt("codigoSupermercado");
        final double valorProduto = getArguments().getDouble("valorProduto");
        String nomeSupermercado = getArguments().getString("nomeSupermercado");
        produto = ProdutoDB.getInstancia(getApplicationContext()).buscarProduto(codigoProduto);

        tvNomeSupermercado.setText(produto.getNome());
        tvNomeSupermercado.setText(nomeSupermercado);
        tvQtde.setText(Integer.toString(quantidade));

        DecimalFormat df = new DecimalFormat("#0.00");
        tvValorProduto.setText("R$ " + df.format(valorProduto));
        tvValorTotal.setText("R$ " + df.format(quantidade*valorProduto));

        btAdicionarAoCarrinho = view.findViewById(R.id.bt_adicionar);
        btAdicionarAoCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProdutoSupermercadoCarrinho produtoSupermercadoCarrinho = new ProdutoSupermercadoCarrinho();
                produtoSupermercadoCarrinho.setProduto(produto);
                produtoSupermercadoCarrinho.setProdutoSupermercado(new ProdutoSupermercado(new Supermercado(codigoSupermercado), valorProduto));
                produtoSupermercadoCarrinho.setQuantidade(quantidade);

                CarrinhoDB.getInstancia(getApplicationContext()).salvarProdutoSupermercadoCarrinho(produtoSupermercadoCarrinho);

                mAdicionarAoCarrinho.onAdicionarAoCarrinho(produtoSupermercadoCarrinho);

                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mAdicionarAoCarrinho = (OnAdicionarAoCarrinhoListener) context;

        } catch (Exception e) {

        }
    }

    public interface OnAdicionarAoCarrinhoListener {
        void onAdicionarAoCarrinho(ProdutoSupermercadoCarrinho produtoSupermercadoCarrinho);
    }
}
