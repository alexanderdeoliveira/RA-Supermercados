package com.rasupermercados.rasupermercados.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.rasupermercados.rasupermercados.negocio.Produto;
import com.rasupermercados.rasupermercados.negocio.Usuario;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDB {
    private static  UsuarioDB instancia;
    private Context contexto;

    public static UsuarioDB getInstancia(Context context) {
        if(instancia == null)
            instancia = new UsuarioDB(context);

        return instancia;
    }

    public UsuarioDB(Context contexto) {
        this.contexto = contexto;
    }

    public Usuario buscarUsuario() {
        BancoDeDados db = BancoDeDados.getInstance(contexto);
        SQLiteDatabase dataBase = db.getReadableDatabase();


        String[] projection = {
                "USNOMEUSUARIO",
                "USURLFOTO",
                "USEMAIL"
        };

        Cursor cursor = dataBase.query(
                "USUARIO",
                projection,
                null,
                null,
                null,
                null,
                null
        );

        Usuario usuario = null;
        if (cursor.moveToFirst()) {
            String nomeUsuario = cursor.getString(cursor.getColumnIndex("USNOMEUSUARIO"));
            String email = cursor.getString(cursor.getColumnIndex("USEMAIL"));
            String urlFoto = cursor.getString(cursor.getColumnIndex("USURLFOTO"));

            Uri uri = Uri.fromFile(new File(urlFoto));

            usuario = new Usuario();
            usuario.setNome(nomeUsuario);
            usuario.setEmail(email);
            usuario.setUrlFoto(uri);

        }

        return usuario;
    }

    public void atualizarUsuario(Usuario usuario) {
        SQLiteDatabase db = BancoDeDados.getInstance(contexto).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("USNOMEUSUARIO", usuario.getNome());
        values.put("USURLFOTO", usuario.getUrlFoto().getPath());
        values.put("USEMAIL", usuario.getEmail());

        db.update("USUARIO", values, null,null);
    }

    public void salvarUsuario(Usuario usuario) {
        SQLiteDatabase db = BancoDeDados.getInstance(contexto).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("USNOMEUSUARIO", usuario.getNome());
        values.put("USURLFOTO", usuario.getUrlFoto().getPath());
        values.put("USEMAIL", usuario.getEmail());

        db.insert("USUARIO", null, values);
    }
}
