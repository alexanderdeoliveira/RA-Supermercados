package com.rasupermercados.rasupermercados.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rasupermercados.rasupermercados.negocio.Categoria;
import com.rasupermercados.rasupermercados.negocio.Produto;

import java.util.ArrayList;
import java.util.List;

public class CategoriaDB {
    private static CategoriaDB instancia;
    private Context contexto;

    public static CategoriaDB getInstancia(Context context) {
        if(instancia == null) {
            instancia = new CategoriaDB(context);
        }

        return instancia;
    }

    public CategoriaDB(Context contexto) {
        this.contexto = contexto;
    }

    public List<Categoria> buscarCategorias() {
        BancoDeDados db = BancoDeDados.getInstance(contexto);
        SQLiteDatabase dataBase = db.getReadableDatabase();


        String[] projection = {
                "CACODCOTEGORIA",
                "CANOMECATEGORIA",
                "CAURLIMAGEMCATEGORIA"
        };

        Cursor cursor = dataBase.query(
                "CATEGORIA",
                projection,
                null,
                null,
                null,
                null,
                null
        );

        List<Categoria> categorias = new ArrayList<>();
        while (cursor.moveToNext()) {
            int codigo = cursor.getInt(cursor.getColumnIndex("CACODCOTEGORIA"));
            Categoria categoria = new Categoria(codigo);
            categoria.setNome(cursor.getString(cursor.getColumnIndex("CANOMECATEGORIA")));

            categorias.add(categoria);
        }

        return categorias;
    }

    public int atualizarCategoria(Categoria categoria) {
        SQLiteDatabase db = BancoDeDados.getInstance(contexto).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("CANOMECATEGORIA", categoria.getNome());

        return db.update("CATEGORIA", values, "CACODCOTEGORIA = " + categoria.getCodigoCategoria(), null);
    }

    public void salvarCategoria(Categoria categoria) {
        SQLiteDatabase db = BancoDeDados.getInstance(contexto).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("CACODCOTEGORIA", categoria.getCodigoCategoria());
        values.put("CANOMECATEGORIA", categoria.getNome());

        db.insert("CATEGORIA", null, values);
    }

    public void deletarCategoria(int codCategoria) {
        SQLiteDatabase db = BancoDeDados.getInstance(contexto).getWritableDatabase();
        db.delete("CATEGORIA","CACODCOTEGORIA = "+codCategoria,null);
    }
}
