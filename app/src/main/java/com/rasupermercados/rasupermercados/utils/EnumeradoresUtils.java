package com.rasupermercados.rasupermercados.utils;

public class EnumeradoresUtils {

    public enum TIPO_CATEGORIA
    {
        ACOUGUE(1), HORTIFRUTI(2);

        private int tipo;

        TIPO_CATEGORIA(int valor)
        {
            tipo = valor;
        }

        public int getStatusCode()
        {
            return tipo;
        }

    }
}
