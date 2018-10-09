package com.rasupermercados.rasupermercados.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rasupermercados.rasupermercados.negocio.Produto;
import com.rasupermercados.rasupermercados.negocio.Usuario;

import java.util.ArrayList;
import java.util.List;

public class ProdutoDB {
    private static ProdutoDB instancia;
    private Context contexto;

    public static ProdutoDB getInstancia(Context context) {
        if(instancia == null) {
            instancia = new ProdutoDB(context);
        }

        return instancia;
    }

    public ProdutoDB(Context contexto) {
        this.contexto = contexto;
    }

    public Produto buscarProduto(int codProduto) {
        BancoDeDados db = BancoDeDados.getInstance(contexto);

        SQLiteDatabase dataBase = db.getWritableDatabase();

        String[] projection = {
                "PRCODPRODUTO",
                "PRNOMEPRODUTO",
                "PRURLFOTOPRODUTO"
        };

        String selection =  "PRCODPRODUTO = " + codProduto;

        Cursor cursor = dataBase.query(
                "PRODUTO",
                projection,
                selection,
                null,
                null,
                null,
                null
        );

        Produto produto = null;

        if (cursor.moveToFirst()) {
            produto = new Produto();
            produto.setCodigo(cursor.getInt(cursor.getColumnIndex("PRCODPRODUTO")));
            produto.setNome(cursor.getString(cursor.getColumnIndex("PRNOMEPRODUTO")));
            produto.setUrlFotoStorage(cursor.getString(cursor.getColumnIndex("PRURLFOTOPRODUTO")));

            cursor.close();
        }

        return produto;
    }

    public List<Produto> buscarProdutos(String query) {
        BancoDeDados db = BancoDeDados.getInstance(contexto);
        SQLiteDatabase dataBase = db.getReadableDatabase();


        String[] projection = {
                "PRCODPRODUTO",
                "PRNOMEPRODUTO",
                "PRURLFOTOPRODUTO"
        };

        String selection =  "PRNOMEPRODUTO LIKE '%"+ query +"%'";


        Cursor cursor = dataBase.query(
                "PRODUTO",
                projection,
                selection,
                null,
                null,
                null,
                null
        );

        List<Produto> produtos = new ArrayList<>();
        while (cursor.moveToNext()) {
            Produto produto = new Produto();
            produto.setCodigo(cursor.getInt(cursor.getColumnIndex("PRCODPRODUTO")));
            produto.setNome(cursor.getString(cursor.getColumnIndex("PRNOMEPRODUTO")));
            produto.setUrlFotoStorage(cursor.getString(cursor.getColumnIndex("PRURLFOTOPRODUTO")));

            produtos.add(produto);
        }

        cursor.close();

        return produtos;
    }

    public List<Produto> buscarProdutos() {
        BancoDeDados db = BancoDeDados.getInstance(contexto);
        SQLiteDatabase dataBase = db.getReadableDatabase();


        String[] projection = {
                "PRCODPRODUTO",
                "PRNOMEPRODUTO",
                "PRURLFOTOPRODUTO"
        };


        Cursor cursor = dataBase.query(
                "PRODUTO",
                projection,
                null,
                null,
                null,
                null,
                null
        );

        List<Produto> produtos = new ArrayList<>();
        while (cursor.moveToNext()) {
            Produto produto = new Produto();
            produto.setCodigo(cursor.getInt(cursor.getColumnIndex("PRCODPRODUTO")));
            produto.setNome(cursor.getString(cursor.getColumnIndex("PRNOMEPRODUTO")));
            produto.setUrlFotoStorage(cursor.getString(cursor.getColumnIndex("PRURLFOTOPRODUTO")));

            produtos.add(produto);
        }

        cursor.close();

        return produtos;
    }

    public void salvarProduto(Produto produto) {
        SQLiteDatabase db = BancoDeDados.getInstance(contexto).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("PRCODPRODUTO", produto.getCodigo());
        values.put("PRNOMEPRODUTO", produto.getNome());
        values.put("PRURLFOTOPRODUTO", produto.getUrlFotoStorage());

        db.insert("PRODUTO", null, values);
    }

    public int atualizarProduto(Produto produto) {
        SQLiteDatabase db = BancoDeDados.getInstance(contexto).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("PRCODPRODUTO", produto.getCodigo());
        values.put("PRNOMEPRODUTO", produto.getNome());
        values.put("PRURLFOTOPRODUTO", produto.getUrlFotoStorage());

        return db.update("PRODUTO", values, "PRCODPRODUTO = " + produto.getCodigo(), null);
    }


    public void deletarProduto(int codProduto) {
        SQLiteDatabase db = BancoDeDados.getInstance(contexto).getWritableDatabase();
        db.delete("PRODUTO","PRCODPRODUTO = "+codProduto,null);
    }
}
