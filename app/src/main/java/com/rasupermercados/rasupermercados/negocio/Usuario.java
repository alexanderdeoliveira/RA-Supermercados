package com.rasupermercados.rasupermercados.negocio;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

import java.net.URI;

public class Usuario {
    String nome;
    Uri urlFoto;
    String email;

    public Usuario(FirebaseUser usuario) {
        this.nome = usuario.getDisplayName();
        this.urlFoto = usuario.getPhotoUrl();
        this.email = usuario.getEmail();
    }

    public Usuario() { }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUrlFoto(Uri urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getEmail() {
        return email;
    }

    public Uri getUrlFoto() {
        return urlFoto;
    }
}
