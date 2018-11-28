package com.rasupermercados.rasupermercados.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rasupermercados.rasupermercados.R;
import com.rasupermercados.rasupermercados.db.UsuarioDB;
import com.rasupermercados.rasupermercados.fragments.BannerFragment;
import com.rasupermercados.rasupermercados.negocio.Categoria;
import com.rasupermercados.rasupermercados.negocio.CategoriaFirebase;
import com.rasupermercados.rasupermercados.negocio.Produto;
import com.rasupermercados.rasupermercados.negocio.ProdutoFirebase;
import com.rasupermercados.rasupermercados.negocio.SupermercadoFirebase;
import com.rasupermercados.rasupermercados.negocio.Usuario;
import com.rasupermercados.rasupermercados.utils.Constantes;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private static final int REQUEST_READ_WRITE_STORAGE = 1;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    private UserLoginTask mAuthTask = null;

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private FirebaseAuth mAuth;
    private CallbackManager callbackManager;
    private ProgressDialog dialogLogin;
    private Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;

        mAuth = FirebaseAuth.getInstance();
        if(buscarUsuarioLogado() == null) {
            setContentView(R.layout.activity_login);
            // Set up the login form.
            mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
            populateAutoComplete();

            mPasswordView = (EditText) findViewById(R.id.password);
            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                        attemptLogin();
                        return true;
                    }
                    return false;
                }
            });

            Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
            mEmailSignInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    //attemptLogin();

                    BannerFragment bannerFragment = new BannerFragment();
                    Bundle extras = new Bundle();
                    extras.putString("nome_imagem", "banner_login.jpeg");
                    bannerFragment.setArguments(extras);
                    bannerFragment.show(getSupportFragmentManager(),"frag_pagamento");
                }
            });

            mLoginFormView = findViewById(R.id.login_form);
            mProgressView = findViewById(R.id.login_progress);

            callbackManager = CallbackManager.Factory.create();

            LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
            loginButton.setReadPermissions("email", "public_profile");

            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    dialogLogin = ProgressDialog.show(activity, "", "Aguarde login...");
                    handleFacebookAccessToken(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {
                    // App code
                }

                @Override
                public void onError(FacebookException exception) {
                    // App code
                }
            });
        } else {
            startActivityForResult(new Intent(getApplicationContext(), MainActivity.class), Constantes.REQUEST_MAIN);
        }

        //enviarProdutos();
        //EnviarPlanilha();
    }

    public void disconnectFromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

            }
        }).executeAsync();
    }

    private Usuario buscarUsuarioLogado() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        UsuarioDB usuarioDB = UsuarioDB.getInstancia(getApplicationContext());
        if(currentUser != null) {
            Usuario usuario = new Usuario(currentUser);
            usuarioDB.atualizarUsuario(usuario);

            return usuario;

        } else
            return usuarioDB.buscarUsuario();
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Usuario usuario = new Usuario(user);
                            UsuarioDB.getInstancia(getApplicationContext()).salvarUsuario(usuario);
                            dialogLogin.dismiss();
                            startActivityForResult(new Intent(getApplicationContext(), MainActivity.class), Constantes.REQUEST_MAIN);
                        } else {
                            Toast.makeText(getApplicationContext(),"Não foi possível fazer login", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void EnviarPlanilha() {
        /*File planilha = new File(Environment.getExternalStorageDirectory(), "Documents/RelatorioProdutos.csv");

        try
        {
            FileInputStream fl = new FileInputStream(planilha);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fl));

            int cont = 0;
            String line;
            List<Produto> produtos = new ArrayList<>();
            Produto produto = new Produto();

            while (cont < 2) {
                while ((line = reader.readLine()) != null)
                {
                    if(!line.replace(";","").equals(""))
                    {
                        String[] dados = line.split(";");
                        if(dados.length > 1) {
                            if(cont == 0 && !dados[0].equals("3/12/18")) {
                                produto.setCodigo(Integer.parseInt(dados[0]));
                                produto.setNome(dados[2]);
                                produto.setUrlFotoStorage("Ketchup.jpeg");

                                produtos.add(produto);
                            } else {
                                if(dados[0].equals("3/12/18")) {
                                    cont = 2;
                                    break;
                                }
                            }

                            cont++;

                            Log.i("Dados", "cont = " + cont +", dados = " +line);
                        }

                    } else {
                        cont = 0;
                        produto = new Produto();

                    }

                }
            }


            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference();

            for(int i =0; i < produtos.size();i++)
            {
                Produto item = produtos.get(i);
                myRef.child("produtos").child(Integer.toString(item.getCodigo())).setValue(item);
            }

            Toast.makeText(getApplicationContext(), "Planilha enviada", Toast.LENGTH_SHORT).show();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Planilha não enviada. Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Planilha não enviada. Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }*/


        /*FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        Supermercado supermercado = new Supermercado();
        supermercado.setCodigo(1);
        supermercado.setNome("Supermercado Catenas");
        myRef.child("supermercados").child(Integer.toString(supermercado.getCodigo())).setValue(supermercado);

        supermercado = new Supermercado();
        supermercado.setCodigo(2);
        supermercado.setNome("Carrefour");
        myRef.child("supermercados").child(Integer.toString(supermercado.getCodigo())).setValue(supermercado);

        supermercado = new Supermercado();
        supermercado.setCodigo(3);
        supermercado.setNome("Super Zé");
        myRef.child("supermercados").child(Integer.toString(supermercado.getCodigo())).setValue(supermercado);

        supermercado = new Supermercado();
        supermercado.setCodigo(4);
        supermercado.setNome("Supermercado Marcos");
        myRef.child("supermercados").child(Integer.toString(supermercado.getCodigo())).setValue(supermercado);

        supermercado = new Supermercado();
        supermercado.setCodigo(5);
        supermercado.setNome("WallMart");
        myRef.child("supermercados").child(Integer.toString(supermercado.getCodigo())).setValue(supermercado);

        supermercado = new Supermercado();
        supermercado.setCodigo(6);
        supermercado.setNome("Prátiko");
        myRef.child("supermercados").child(Integer.toString(supermercado.getCodigo())).setValue(supermercado);

        supermercado = new Supermercado();
        supermercado.setCodigo(7);
        supermercado.setNome("Leve");
        myRef.child("supermercados").child(Integer.toString(supermercado.getCodigo())).setValue(supermercado);

        supermercado = new Supermercado();
        supermercado.setCodigo(8);
        supermercado.setNome("Supermercado Barros");
        myRef.child("supermercados").child(Integer.toString(supermercado.getCodigo())).setValue(supermercado);*/


        /*File planilha = new File(Environment.getExternalStorageDirectory(), "Documents/RelatorioProdutos.csv");

        try
        {
            FileInputStream fl = new FileInputStream(planilha);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fl));

            int cont = 0;
            String line;
            List<ProdutoSupermercado> listaProdutoSupermercado = new ArrayList<>();
            ProdutoSupermercado produtoSupermercado = new ProdutoSupermercado();

            while (cont < 2) {
                while ((line = reader.readLine()) != null)
                {
                    if(!line.replace(";","").equals(""))
                    {
                        String[] dados = line.split(";");
                        if(dados.length > 1) {

                            if(cont == 0 && !dados[0].equals("3/12/18")) {
                                produtoSupermercado = new ProdutoSupermercado();
                                Produto produto = new Produto();
                                produto.setCodigo(Integer.parseInt(dados[0]));
                                produto.setNome(dados[2]);
                                produto.setUrlFotoStorage("Ketchup.jpeg");

                                produtoSupermercado.setProduto(produto);

                                Supermercado supermercado = new Supermercado();
                                supermercado.setCodigo(1);
                                supermercado.setNome("Supermercado Catenas");

                                produtoSupermercado.setSupermercado(supermercado);

                            } else {
                                if(dados[0].equals("3/12/18")) {
                                    cont = 2;
                                    break;
                                }else {
                                    produtoSupermercado.setValor(Double.parseDouble(dados[3].replace(".", "").replace(",", ".")));
                                    listaProdutoSupermercado.add(produtoSupermercado);
                                }
                            }

                            cont++;

                            Log.i("Dados", "cont = " + cont +", dados = " +line);
                        }

                    } else {
                        cont = 0;
                        produtoSupermercado = new ProdutoSupermercado();

                    }

                }
            }


            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference();

            for(int i =0; i < listaProdutoSupermercado.size();i++)
            {
                ProdutoSupermercado item = listaProdutoSupermercado.get(i);
                myRef.child("produto_supermercado").child(Integer.toString(item.getProduto().getCodigo())).child("2").setValue(item.getValor()+0.15);

                myRef.child("produto_supermercado").child(Integer.toString(item.getProduto().getCodigo())).child("3").setValue(item.getValor()+0.3);

                myRef.child("produto_supermercado").child(Integer.toString(item.getProduto().getCodigo())).child("4").setValue(item.getValor()+0.45);

                myRef.child("produto_supermercado").child(Integer.toString(item.getProduto().getCodigo())).child("5").setValue(item.getValor()+1);

                myRef.child("produto_supermercado").child(Integer.toString(item.getProduto().getCodigo())).child("6").setValue(item.getValor()+1.15);

                myRef.child("produto_supermercado").child(Integer.toString(item.getProduto().getCodigo())).child("7").setValue(item.getValor()+1.15);

                myRef.child("produto_supermercado").child(Integer.toString(item.getProduto().getCodigo())).child("8").setValue(item.getValor()+1.25);

            }

            Toast.makeText(getApplicationContext(), "Planilha enviada", Toast.LENGTH_SHORT).show();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Planilha não enviada. Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Planilha não enviada. Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }*/

    }

    private void enviarProdutos() {
        //AÇOUGUE
        /*FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        CategoriaFirebase categoria = new CategoriaFirebase();
        categoria.setCodigoCategoria(1);

        ProdutoFirebase produto = new ProdutoFirebase();
        produto.setCategoria(categoria);
        produto.setCodigo(1);
        produto.setNome("Peça de Picanha Bovina Congelada 1kg");
        produto.setUrlFotoStorage("Picanha.jpg");

        double valor = 60.00;
        for (int i=1;i<9;i++) {
            SupermercadoFirebase supermercadoFirebase = new SupermercadoFirebase();
            supermercadoFirebase.setCodigo(i);
            supermercadoFirebase.setValor(valor+(i*0.30));
            produto.addSupermercado(supermercadoFirebase);
        }

        myRef.child("produtos").child(Integer.toString(produto.getCodigo())).setValue(produto);

        produto = new ProdutoFirebase();
        produto.setCategoria(categoria);
        produto.setCodigo(6);
        produto.setNome("Bife de Contra Filé Bovino Entrecote Resfriado 1,3kg");
        produto.setUrlFotoStorage("ContraFile.jpg");

        valor = 50.19;
        for (int i=1;i<9;i++) {
            SupermercadoFirebase supermercadoFirebase = new SupermercadoFirebase();
            supermercadoFirebase.setCodigo(i);
            supermercadoFirebase.setValor(valor+(i*0.30));
            produto.addSupermercado(supermercadoFirebase);
        }
        myRef.child("produtos").child(Integer.toString(produto.getCodigo())).setValue(produto);

        produto = new ProdutoFirebase();
        produto.setCategoria(categoria);
        produto.setCodigo(11);
        produto.setNome("Linguiça Calabresa com Pimenta 600g");
        produto.setUrlFotoStorage("LinguicaCalabresa.jpg");

        valor = 25.69;
        for (int i=1;i<9;i++) {
            SupermercadoFirebase supermercadoFirebase = new SupermercadoFirebase();
            supermercadoFirebase.setCodigo(i);
            supermercadoFirebase.setValor(valor+(i*0.30));
            produto.addSupermercado(supermercadoFirebase);
        }
        myRef.child("produtos").child(Integer.toString(produto.getCodigo())).setValue(produto);

        produto = new ProdutoFirebase();
        produto.setCategoria(categoria);
        produto.setCodigo(16);
        produto.setNome("Linguiça de Frango com Bacon 600g");
        produto.setUrlFotoStorage("LinguicaFrango.jpg");

        valor = 19.89;
        for (int i=1;i<9;i++) {
            SupermercadoFirebase supermercadoFirebase = new SupermercadoFirebase();
            supermercadoFirebase.setCodigo(i);
            supermercadoFirebase.setValor(valor+(i*0.30));
            produto.addSupermercado(supermercadoFirebase);
        }
        myRef.child("produtos").child(Integer.toString(produto.getCodigo())).setValue(produto);

        produto = new ProdutoFirebase();
        produto.setCategoria(categoria);
        produto.setCodigo(21);
        produto.setNome("Costela Congelada com Osso 1kg");
        produto.setUrlFotoStorage("Costela.jpg");

        valor = 29.90;
        for (int i=1;i<9;i++) {
            SupermercadoFirebase supermercadoFirebase = new SupermercadoFirebase();
            supermercadoFirebase.setCodigo(i);
            supermercadoFirebase.setValor(valor+(i*0.30));
            produto.addSupermercado(supermercadoFirebase);
        }
        myRef.child("produtos").child(Integer.toString(produto.getCodigo())).setValue(produto);*/

        //HORTIFRUTI
        /*FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        CategoriaFirebase categoria = new CategoriaFirebase();
        categoria.setCodigoCategoria(2);

        ProdutoFirebase produto = new ProdutoFirebase();
        produto.setCategoria(categoria);
        produto.setCodigo(2);
        produto.setNome("Laranja Pera 500g");
        produto.setUrlFotoStorage("Laranja.jpg");

        double valor = 1.39;
        for (int i=1;i<9;i++) {
            SupermercadoFirebase supermercadoFirebase = new SupermercadoFirebase();
            supermercadoFirebase.setCodigo(i);
            supermercadoFirebase.setValor(valor+(i*0.30));
            produto.addSupermercado(supermercadoFirebase);
        }

        myRef.child("produtos").child(Integer.toString(produto.getCodigo())).setValue(produto);

        produto = new ProdutoFirebase();
        produto.setCategoria(categoria);
        produto.setCodigo(7);
        produto.setNome("Manga Palmer 1kg");
        produto.setUrlFotoStorage("Manga.jpg");

        valor = 8.99;
        for (int i=1;i<9;i++) {
            SupermercadoFirebase supermercadoFirebase = new SupermercadoFirebase();
            supermercadoFirebase.setCodigo(i);
            supermercadoFirebase.setValor(valor+(i*0.30));
            produto.addSupermercado(supermercadoFirebase);
        }
        myRef.child("produtos").child(Integer.toString(produto.getCodigo())).setValue(produto);

        produto = new ProdutoFirebase();
        produto.setCategoria(categoria);
        produto.setCodigo(12);
        produto.setNome("Alface Lisa Orgânico 100g");
        produto.setUrlFotoStorage("Alface.jpg");

        valor = 6.99;
        for (int i=1;i<9;i++) {
            SupermercadoFirebase supermercadoFirebase = new SupermercadoFirebase();
            supermercadoFirebase.setCodigo(i);
            supermercadoFirebase.setValor(valor+(i*0.30));
            produto.addSupermercado(supermercadoFirebase);
        }
        myRef.child("produtos").child(Integer.toString(produto.getCodigo())).setValue(produto);

        produto = new ProdutoFirebase();
        produto.setCategoria(categoria);
        produto.setCodigo(17);
        produto.setNome("Tomate 500g");
        produto.setUrlFotoStorage("Tomate.jpg");

        valor = 4.29;
        for (int i=1;i<9;i++) {
            SupermercadoFirebase supermercadoFirebase = new SupermercadoFirebase();
            supermercadoFirebase.setCodigo(i);
            supermercadoFirebase.setValor(valor+(i*0.30));
            produto.addSupermercado(supermercadoFirebase);
        }
        myRef.child("produtos").child(Integer.toString(produto.getCodigo())).setValue(produto);

        produto = new ProdutoFirebase();
        produto.setCategoria(categoria);
        produto.setCodigo(22);
        produto.setNome("Pepino Comum 500g");
        produto.setUrlFotoStorage("Pepino.jpg");

        valor = 3.19;
        for (int i=1;i<9;i++) {
            SupermercadoFirebase supermercadoFirebase = new SupermercadoFirebase();
            supermercadoFirebase.setCodigo(i);
            supermercadoFirebase.setValor(valor+(i*0.30));
            produto.addSupermercado(supermercadoFirebase);
        }
        myRef.child("produtos").child(Integer.toString(produto.getCodigo())).setValue(produto);*/

        //BEBIDAS
        /*FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        CategoriaFirebase categoria = new CategoriaFirebase();
        categoria.setCodigoCategoria(3);

        ProdutoFirebase produto = new ProdutoFirebase();
        produto.setCategoria(categoria);
        produto.setCodigo(3);
        produto.setNome("Fanta Laranja 2 Litros");
        produto.setUrlFotoStorage("FantaLaranja.jpg");

        double valor = 4.49;
        for (int i=1;i<9;i++) {
            SupermercadoFirebase supermercadoFirebase = new SupermercadoFirebase();
            supermercadoFirebase.setCodigo(i);
            supermercadoFirebase.setValor(valor+(i*0.30));
            produto.addSupermercado(supermercadoFirebase);
        }

        myRef.child("produtos").child(Integer.toString(produto.getCodigo())).setValue(produto);

        produto = new ProdutoFirebase();
        produto.setCategoria(categoria);
        produto.setCodigo(8);
        produto.setNome("Coca-Cola 1,5 Litros");
        produto.setUrlFotoStorage("Coca.jpg");

        valor = 6.49;
        for (int i=1;i<9;i++) {
            SupermercadoFirebase supermercadoFirebase = new SupermercadoFirebase();
            supermercadoFirebase.setCodigo(i);
            supermercadoFirebase.setValor(valor+(i*0.30));
            produto.addSupermercado(supermercadoFirebase);
        }
        myRef.child("produtos").child(Integer.toString(produto.getCodigo())).setValue(produto);

        produto = new ProdutoFirebase();
        produto.setCategoria(categoria);
        produto.setCodigo(13);
        produto.setNome("Powerade Sabor Mix de Frutas 500ml");
        produto.setUrlFotoStorage("Powerade.jpg");

        valor = 3.59;
        for (int i=1;i<9;i++) {
            SupermercadoFirebase supermercadoFirebase = new SupermercadoFirebase();
            supermercadoFirebase.setCodigo(i);
            supermercadoFirebase.setValor(valor+(i*0.30));
            produto.addSupermercado(supermercadoFirebase);
        }
        myRef.child("produtos").child(Integer.toString(produto.getCodigo())).setValue(produto);

        produto = new ProdutoFirebase();
        produto.setCategoria(categoria);
        produto.setCodigo(18);
        produto.setNome("Whisky Johnnie Walker Red Label 1 Litro");
        produto.setUrlFotoStorage("RedLabel.jpg");

        valor = 99.90;
        for (int i=1;i<9;i++) {
            SupermercadoFirebase supermercadoFirebase = new SupermercadoFirebase();
            supermercadoFirebase.setCodigo(i);
            supermercadoFirebase.setValor(valor+(i*0.30));
            produto.addSupermercado(supermercadoFirebase);
        }
        myRef.child("produtos").child(Integer.toString(produto.getCodigo())).setValue(produto);

        produto = new ProdutoFirebase();
        produto.setCategoria(categoria);
        produto.setCodigo(23);
        produto.setNome("Cerveja Brahma Pilsen Lager 350ml");
        produto.setUrlFotoStorage("Brahma.jpg");

        valor = 2.59;
        for (int i=1;i<9;i++) {
            SupermercadoFirebase supermercadoFirebase = new SupermercadoFirebase();
            supermercadoFirebase.setCodigo(i);
            supermercadoFirebase.setValor(valor+(i*0.30));
            produto.addSupermercado(supermercadoFirebase);
        }
        myRef.child("produtos").child(Integer.toString(produto.getCodigo())).setValue(produto);*/

        //HIGIENE PESSOAL

        /*FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        CategoriaFirebase categoria = new CategoriaFirebase();
        categoria.setCodigoCategoria(4);

        ProdutoFirebase produto = new ProdutoFirebase();
        produto.setCategoria(categoria);
        produto.setCodigo(4);
        produto.setNome("Papel Higiênico Folha Dupla 30 Metros 16 Rolos");
        produto.setUrlFotoStorage("PapelHigienico.jpg");

        double valor = 27.99;
        for (int i=1;i<9;i++) {
            SupermercadoFirebase supermercadoFirebase = new SupermercadoFirebase();
            supermercadoFirebase.setCodigo(i);
            supermercadoFirebase.setValor(valor+(i*0.30));
            produto.addSupermercado(supermercadoFirebase);
        }

        myRef.child("produtos").child(Integer.toString(produto.getCodigo())).setValue(produto);

        produto = new ProdutoFirebase();
        produto.setCategoria(categoria);
        produto.setCodigo(9);
        produto.setNome("Shampoo Infantil Johnson's Baby Original 400ml");
        produto.setUrlFotoStorage("Shampoo.jpg");

        valor = 15.58;
        for (int i=1;i<9;i++) {
            SupermercadoFirebase supermercadoFirebase = new SupermercadoFirebase();
            supermercadoFirebase.setCodigo(i);
            supermercadoFirebase.setValor(valor+(i*0.30));
            produto.addSupermercado(supermercadoFirebase);
        }
        myRef.child("produtos").child(Integer.toString(produto.getCodigo())).setValue(produto);

        produto = new ProdutoFirebase();
        produto.setCategoria(categoria);
        produto.setCodigo(14);
        produto.setNome("Sabonete em Barra Corporal Dove Original 90g 6 Unidades");
        produto.setUrlFotoStorage("Dove.jpg");

        valor = 12.49;
        for (int i=1;i<9;i++) {
            SupermercadoFirebase supermercadoFirebase = new SupermercadoFirebase();
            supermercadoFirebase.setCodigo(i);
            supermercadoFirebase.setValor(valor+(i*0.30));
            produto.addSupermercado(supermercadoFirebase);
        }
        myRef.child("produtos").child(Integer.toString(produto.getCodigo())).setValue(produto);

        produto = new ProdutoFirebase();
        produto.setCategoria(categoria);
        produto.setCodigo(19);
        produto.setNome("Sabonete Líquido Corporal Nivea Creme Soft Óleo de Amêndoa 250ml");
        produto.setUrlFotoStorage("SaboneteLiquido.jpg");

        valor = 7.69;
        for (int i=1;i<9;i++) {
            SupermercadoFirebase supermercadoFirebase = new SupermercadoFirebase();
            supermercadoFirebase.setCodigo(i);
            supermercadoFirebase.setValor(valor+(i*0.30));
            produto.addSupermercado(supermercadoFirebase);
        }
        myRef.child("produtos").child(Integer.toString(produto.getCodigo())).setValue(produto);

        produto = new ProdutoFirebase();
        produto.setCategoria(categoria);
        produto.setCodigo(24);
        produto.setNome("Haste Flexível 75 Unidades");
        produto.setUrlFotoStorage("HasteFlexivel.jpg");

        valor = 3.39;
        for (int i=1;i<9;i++) {
            SupermercadoFirebase supermercadoFirebase = new SupermercadoFirebase();
            supermercadoFirebase.setCodigo(i);
            supermercadoFirebase.setValor(valor+(i*0.30));
            produto.addSupermercado(supermercadoFirebase);
        }
        myRef.child("produtos").child(Integer.toString(produto.getCodigo())).setValue(produto);*/

        //PADARIA

        /*FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        CategoriaFirebase categoria = new CategoriaFirebase();
        categoria.setCodigoCategoria(5);

        ProdutoFirebase produto = new ProdutoFirebase();
        produto.setCategoria(categoria);
        produto.setCodigo(5);
        produto.setNome("Pão de Forma 12 Grãos Zero Pullman 350g");
        produto.setUrlFotoStorage("PaoForma.jpg");

        double valor = 8.59;
        for (int i=1;i<9;i++) {
            SupermercadoFirebase supermercadoFirebase = new SupermercadoFirebase();
            supermercadoFirebase.setCodigo(i);
            supermercadoFirebase.setValor(valor+(i*0.30));
            produto.addSupermercado(supermercadoFirebase);
        }

        myRef.child("produtos").child(Integer.toString(produto.getCodigo())).setValue(produto);

        produto = new ProdutoFirebase();
        produto.setCategoria(categoria);
        produto.setCodigo(10);
        produto.setNome("Pão de Queijo Massa Leve 400g");
        produto.setUrlFotoStorage("PaoQueijo.jpg");

        valor = 6.29;
        for (int i=1;i<9;i++) {
            SupermercadoFirebase supermercadoFirebase = new SupermercadoFirebase();
            supermercadoFirebase.setCodigo(i);
            supermercadoFirebase.setValor(valor+(i*0.30));
            produto.addSupermercado(supermercadoFirebase);
        }
        myRef.child("produtos").child(Integer.toString(produto.getCodigo())).setValue(produto);

        produto = new ProdutoFirebase();
        produto.setCategoria(categoria);
        produto.setCodigo(15);
        produto.setNome("Bolo de Frappé Califórnia Pullman 250g");
        produto.setUrlFotoStorage("BoloCalifornia.jpg");

        valor = 6.59;
        for (int i=1;i<9;i++) {
            SupermercadoFirebase supermercadoFirebase = new SupermercadoFirebase();
            supermercadoFirebase.setCodigo(i);
            supermercadoFirebase.setValor(valor+(i*0.30));
            produto.addSupermercado(supermercadoFirebase);
        }
        myRef.child("produtos").child(Integer.toString(produto.getCodigo())).setValue(produto);

        produto = new ProdutoFirebase();
        produto.setCategoria(categoria);
        produto.setCodigo(20);
        produto.setNome("Bolo de Chocolate Casa Suíça Zero 280g");
        produto.setUrlFotoStorage("BoloChocolate.jpg");

        valor = 17.69;
        for (int i=1;i<9;i++) {
            SupermercadoFirebase supermercadoFirebase = new SupermercadoFirebase();
            supermercadoFirebase.setCodigo(i);
            supermercadoFirebase.setValor(valor+(i*0.30));
            produto.addSupermercado(supermercadoFirebase);
        }
        myRef.child("produtos").child(Integer.toString(produto.getCodigo())).setValue(produto);

        produto = new ProdutoFirebase();
        produto.setCategoria(categoria);
        produto.setCodigo(25);
        produto.setNome("Mini Pão Francês Integral Light Congelado Brico Bread 300g");
        produto.setUrlFotoStorage("MiniPaoFrances.jpg");

        valor = 6.19;
        for (int i=1;i<9;i++) {
            SupermercadoFirebase supermercadoFirebase = new SupermercadoFirebase();
            supermercadoFirebase.setCodigo(i);
            supermercadoFirebase.setValor(valor+(i*0.30));
            produto.addSupermercado(supermercadoFirebase);
        }
        myRef.child("produtos").child(Integer.toString(produto.getCodigo())).setValue(produto);*/
    }

    private void VerificarPermissao() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE )
                != PackageManager.PERMISSION_GRANTED ||  ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE )
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        0);
            }
        }
        else
            EnviarPlanilha();
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constantes.REQUEST_MAIN) {
            if(resultCode == Constantes.RESULT_REQUEST_LOGOUT) {
                disconnectFromFacebook();
            }
        } else
            callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }else if(requestCode == REQUEST_READ_WRITE_STORAGE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                EnviarPlanilha();

            } else {

                Toast.makeText(getApplicationContext(), "Planilha não enviada. Permissão não concedida", Toast.LENGTH_LONG).show();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

