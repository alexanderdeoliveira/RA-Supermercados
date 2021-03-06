package com.rasupermercados.rasupermercados.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rasupermercados.rasupermercados.negocio.Categoria;
import com.rasupermercados.rasupermercados.negocio.Produto;
import com.rasupermercados.rasupermercados.negocio.ProdutoSupermercado;
import com.rasupermercados.rasupermercados.negocio.Supermercado;
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

            produto.setProdutosSupermercado(buscarProdutosSupermercado(produto.getCodigo()));

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
                "PRURLFOTOPRODUTO",
                "PRCODCATEGORIA"
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
            produto.setCategoria(new Categoria(cursor.getInt(cursor.getColumnIndex("PRCODCATEGORIA"))));
            produto.setProdutosSupermercado(buscarProdutosSupermercado(produto.getCodigo()));

            produtos.add(produto);
        }

        cursor.close();

        return produtos;
    }

    public List<ProdutoSupermercado> buscarProdutosSupermercado(int codigoProduto) {
        BancoDeDados db = BancoDeDados.getInstance(contexto);
        SQLiteDatabase dataBase = db.getReadableDatabase();

        String[] projection = {
                "PSCODSUPERMERCADO",
                "PSVALOR"
        };

        String selection =  "PSCODPRODUTO =  "+ codigoProduto;

        Cursor cursor = dataBase.query(
                "PRODUTOSUPERMERCADO",
                projection,
                selection,
                null,
                null,
                null,
                null
        );

        List<ProdutoSupermercado> produtosSupermercado = new ArrayList<>();
        while (cursor.moveToNext()) {
            Supermercado supermercado = new Supermercado(cursor.getInt(cursor.getColumnIndex("PSCODSUPERMERCADO")));
            ProdutoSupermercado produtoSupermercado = new ProdutoSupermercado(supermercado, cursor.getDouble(cursor.getColumnIndex("PSVALOR")));

            produtosSupermercado.add(produtoSupermercado);
        }

        cursor.close();

        return produtosSupermercado;
    }

    public void salvarProduto(Produto produto) {
        SQLiteDatabase db = BancoDeDados.getInstance(contexto).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("PRCODPRODUTO", produto.getCodigo());
        values.put("PRNOMEPRODUTO", produto.getNome());
        values.put("PRURLFOTOPRODUTO", produto.getUrlFotoStorage());
        values.put("PRCODCATEGORIA", produto.getCategoria().getCodigoCategoria());

        db.insert("PRODUTO", null, values);

        for(int i =0; i< produto.getProdutosSupermercado().size();i++) {
            values = new ContentValues();
            values.put("PSCODPRODUTO", produto.getCodigo());
            values.put("PSCODSUPERMERCADO", produto.getProdutosSupermercado().get(i).getSupermercado().getCodigo());
            values.put("PSVALOR", produto.getProdutosSupermercado().get(i).getValor());

            db.insert("PRODUTOSUPERMERCADO", null, values);
        }

    }

    public int atualizarProduto(Produto produto) {
        SQLiteDatabase db = BancoDeDados.getInstance(contexto).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("PRCODPRODUTO", produto.getCodigo());
        values.put("PRNOMEPRODUTO", produto.getNome());
        values.put("PRURLFOTOPRODUTO", produto.getUrlFotoStorage());
        values.put("PRCODCATEGORIA", produto.getCategoria().getCodigoCategoria());

        return db.update("PRODUTO", values, "PRCODPRODUTO = " + produto.getCodigo(), null);
    }


    public void deletarProduto(int codProduto) {
        SQLiteDatabase db = BancoDeDados.getInstance(contexto).getWritableDatabase();
        db.delete("PRODUTO","PRCODPRODUTO = "+codProduto,null);
    }

    public List<Produto> buscarProdutosPorCategoria(List<Categoria> filtros) {
        BancoDeDados db = BancoDeDados.getInstance(contexto);
        SQLiteDatabase dataBase = db.getReadableDatabase();


        String[] projection = {
                "PRCODPRODUTO",
                "PRNOMEPRODUTO",
                "PRURLFOTOPRODUTO",
                "PRCODCATEGORIA"
        };

        String codigosCategorias = "0";
        for (int i=0;i<filtros.size();i++) {
            codigosCategorias = codigosCategorias.concat(" , " + Integer.toString(filtros.get(i).getCodigoCategoria()));
        }

        String selection = "PRCODCATEGORIA IN ("+ codigosCategorias +")";



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
            produto.setCategoria(new Categoria(cursor.getInt(cursor.getColumnIndex("PRCODCATEGORIA"))));

            produto.setProdutosSupermercado(buscarProdutosSupermercado(produto.getCodigo()));

            produtos.add(produto);
        }

        cursor.close();

        return produtos;
    }
}