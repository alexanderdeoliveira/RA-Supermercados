package com.rasupermercados.rasupermercados.db;

import android.content.Context;

import com.rasupermercados.rasupermercados.negocio.Usuario;

public class UsuarioDB {
    private UsuarioDB instancia;
    private Context contexto;
    public UsuarioDB getInstancia(Context context) {
        if(instancia == null)
            instancia = new UsuarioDB(context);

        return instancia;
    }

    public UsuarioDB(Context contexto) {
        this.contexto = contexto;
    }

    public Usuario buscarUsuario() {
        Usuario usuario = new Usuario();

        return usuario;
    }
}
