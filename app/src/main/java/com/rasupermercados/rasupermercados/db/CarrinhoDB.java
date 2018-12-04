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
import com.rasupermercados.rasupermercados.negocio.SupermercadoCarrinho;

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
                "PCCODSUPERMERCADO"
        );

        Carrinho carrinho = new Carrinho();
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

    public List<SupermercadoCarrinho> buscarSupermercadoCarrinho() {
        BancoDeDados db = BancoDeDados.getInstance(contexto);

        SQLiteDatabase dataBase = db.getWritableDatabase();

        String[] projection = {
                "PCCODSUPERMERCADO",
                "SUM(PCQUANTIDADE) AS QUANTIDADE"
        };

        Cursor cursor = dataBase.query(
                "PRODUTOSUPERMERCADOCARRINHO",
                projection,
                null,
                null,
                "PCCODSUPERMERCADO",
                null,
                "QUANTIDADE DESC"
        );

        List<SupermercadoCarrinho> lista = new ArrayList<>();
        while (cursor.moveToNext()) {
            SupermercadoCarrinho supermercadoCarrinho = new SupermercadoCarrinho();
            supermercadoCarrinho.setSupermercado(new Supermercado(cursor.getInt(cursor.getColumnIndex("PCCODSUPERMERCADO"))));
            supermercadoCarrinho.setQtdProdutos(cursor.getInt(cursor.getColumnIndex("QUANTIDADE")));
            lista.add(supermercadoCarrinho);
        }

        cursor.close();

        return lista;
    }

    public void salvarProdutoSupermercadoCarrinho(ProdutoSupermercadoCarrinho produtoSupermercadoCarrinho) {
        SQLiteDatabase db = BancoDeDados.getInstance(contexto).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("PCCODPRODUTO", produtoSupermercadoCarrinho.getProduto().getCodigo());
        values.put("PCQUANTIDADE", produtoSupermercadoCarrinho.getQuantidade());
        values.put("PCCODSUPERMERCADO", produtoSupermercadoCarrinho.getProdutoSupermercado().getSupermercado().getCodigo());

        db.insert("PRODUTOSUPERMERCADOCARRINHO", null, values);
    }

    public int atualizarQuantidadeProdutoCarrinho(ProdutoSupermercadoCarrinho produtoSupermercadoCarrinho) {
        SQLiteDatabase db = BancoDeDados.getInstance(contexto).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("PCQUANTIDADE", produtoSupermercadoCarrinho.getQuantidade());

        String whereClause = "PCCODPRODUTO = " + produtoSupermercadoCarrinho.getProduto().getCodigo() + " AND PCCODSUPERMERCADO = " + produtoSupermercadoCarrinho.getProdutoSupermercado().getSupermercado().getCodigo();

        return db.update("PRODUTOSUPERMERCADOCARRINHO", values, whereClause, null);
    }

    public int addQuantidadeProdutoCarrinho(ProdutoSupermercadoCarrinho produtoSupermercadoCarrinho) {
        SQLiteDatabase db = BancoDeDados.getInstance(contexto).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("PCQUANTIDADE ", produtoSupermercadoCarrinho.getQuantidade());

        String whereClause = "PCCODPRODUTO = " + produtoSupermercadoCarrinho.getProduto().getCodigo() + " AND PCCODSUPERMERCADO = " + produtoSupermercadoCarrinho.getProdutoSupermercado().getSupermercado().getCodigo();

        return db.update("PRODUTOSUPERMERCADOCARRINHO", values, whereClause, null);
    }

    public int getQuantidadeProduto(ProdutoSupermercadoCarrinho produtoSupermercadoCarrinho) {
        SQLiteDatabase db = BancoDeDados.getInstance(contexto).getWritableDatabase();

        int retorno =0;

        String[] projection = {
                "PCQUANTIDADE"
        };

        Cursor cursor = db.query(
                "PRODUTOSUPERMERCADOCARRINHO",
                projection,
                null,
                null,
                "PCCODSUPERMERCADO",
                null,
                null
        );

        List<SupermercadoCarrinho> lista = new ArrayList<>();
        if (cursor.moveToNext()) {
            retorno = cursor.getInt(0);
        }

        cursor.close();

        return retorno;
    }

    public int deletarProdutoCarrinho(ProdutoSupermercadoCarrinho produtoSupermercadoCarrinho) {
        SQLiteDatabase db = BancoDeDados.getInstance(contexto).getWritableDatabase();

        String where = "PCCODPRODUTO = "+produtoSupermercadoCarrinho.getProduto().getCodigo() + " AND PCCODSUPERMERCADO = " + produtoSupermercadoCarrinho.getProdutoSupermercado().getSupermercado().getCodigo();
        return db.delete("PRODUTOSUPERMERCADOCARRINHO",where,null);
    }

    public void deletarCarrinho() {
        SQLiteDatabase db = BancoDeDados.getInstance(contexto).getWritableDatabase();

        db.delete("PRODUTOSUPERMERCADOCARRINHO",null,null);
    }
}