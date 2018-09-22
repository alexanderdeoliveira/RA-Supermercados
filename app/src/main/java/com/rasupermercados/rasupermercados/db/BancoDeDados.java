package com.rasupermercados.rasupermercados.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BancoDeDados extends SQLiteOpenHelper {

        protected Context context;
        private static BancoDeDados	instancia;

        public BancoDeDados(Context context) {
            super(context, "rasupermercados.db", null, 1);
            this.context = context;
        }

        public static synchronized BancoDeDados getInstance(Context context) {
            if (instancia == null)
                instancia = new BancoDeDados(context);
            return instancia;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE USUARIO(" +
                    "  USNOMEUSUARIO    INTEGER" +
                    ", USSENHA    TEXT     " +
                    ", USEMAIL  TEXT     " +
                    ")");

            db.execSQL("CREATE TABLE CARRINHO(" +
                    "  CACODCARRINHO    INTEGER" +
                    ")");

            db.execSQL("CREATE TABLE PRODUTO(" +
                    "  PRCODPRODUTO    INTEGER" +
                    ", PRNOMEPRODUTO    TEXT     " +
                    ")");

            db.execSQL("CREATE TABLE CATEGORIA(" +
                    "  CACODCOTEGORIA    INTEGER" +
                    ", CANOMECATEGORIA    TEXT     " +
                    ")");

            db.execSQL("CREATE TABLE PRODUTOSUPERMERCADO(" +
                    "  PSCODPRODUTO    INTEGER " +
                    ", PSCODSUPERMERCADO    INTEGER " +
                    ", PSVALOR    INTEGER     " +
                    ")");


            db.execSQL("CREATE TABLE SUPERMERCADO(" +
                    "  SUCODSUPERMERCADO    INTEGER" +
                    ", SUNOMESUPERMERCADO    INTEGER" +
                    ")");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
}
