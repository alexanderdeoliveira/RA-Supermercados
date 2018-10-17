package com.rasupermercados.rasupermercados.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rasupermercados.rasupermercados.negocio.Carrinho;
import com.rasupermercados.rasupermercados.negocio.Categoria;
import com.rasupermercados.rasupermercados.negocio.Produto;
import com.rasupermercados.rasupermercados.negocio.ProdutoSupermercado;
import com.rasupermercados.rasupermercados.negocio.ProdutoSupermercadoCarrinho;
import com.rasupermercados.rasupermercados.negocio.Supermercado;

import java.util.ArrayList;
import java.util.List;

public class CarrinhoDB {
    private static CarrinhoDB instancia;
    private Context contexto;

    public static CarrinhoDB getInstancia(Context context) {
        if(instancia == null) {
            instancia = new CarrinhoDB(context);
        }

        return instancia;
    }

    public CarrinhoDB(Context contexto) {
        this.contexto = contexto;
    }

    public Carrinho buscarCarrinho() {
        BancoDeDados db = BancoDeDados.getInstance(contexto);

        SQLiteDatabase dataBase = db.getWritableDatabase();

        String[] projection = {
                "PCCODPRODUTO",
                "PCQUANTIDADE",
                "PCCODSUPERMERCADO",
                "PRNOMEPRODUTO",
                "PSVALOR"
        };

        String selection =  "PCCODPRODUTO = PRCODPRODUTO AND PRCODPRODUTO = PSCODPRODUTO AND PSCODSUPERMERCADO = PCCODSUPERMERCADO";

        Cursor cursor = dataBase.query(
                "PRODUTOSUPERMERCADOCARRINHO, PRODUTO, PRODUTOSUPERMERCADO",
                projection,
                selection,
                null,
                null,
                null,
                null
        );

        Carrinho carrinho = new Carrinho();
        carrinho.setValorTotal(0);
        while (cursor.moveToNext()) {
            ProdutoSupermercadoCarrinho produtoSupermercadoCarrinho = new ProdutoSupermercadoCarrinho();
            produtoSupermercadoCarrinho.setProduto(new Produto(cursor.getInt(cursor.getColumnIndex("PCCODPRODUTO")), cursor.getString(cursor.getColumnIndex("PRNOMEPRODUTO"))));
            produtoSupermercadoCarrinho.setProdutoSupermercado(new ProdutoSupermercado(new Supermercado(cursor.getInt(cursor.getColumnIndex("PCCODSUPERMERCADO"))), cursor.getDouble(cursor.getColumnIndex("PSVALOR"))));
            produtoSupermercadoCarrinho.setQuantidade(cursor.getInt(cursor.getColumnIndex("PCQUANTIDADE")));

            carrinho.addProdutoSupermercadoCarrinho(produtoSupermercadoCarrinho);
        }

        cursor.close();

        return carrinho;
    }

    public void salvarProdutoSupermercadoCarrinho(ProdutoSupermercadoCarrinho produtoSupermercadoCarrinho) {
        SQLiteDatabase db = BancoDeDados.getInstance(contexto).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("PCCODPRODUTO", produtoSupermercadoCarrinho.getProduto().getCodigo());
        values.put("PCQUANTIDADE", produtoSupermercadoCarrinho.getQuantidade());
        values.put("PCCODSUPERMERCADO", produtoSupermercadoCarrinho.getProdutoSupermercado().getSupermercado().getCodigo());

        db.insert("PRODUTOSUPERMERCADOCARRINHO", null, values);
    }
}