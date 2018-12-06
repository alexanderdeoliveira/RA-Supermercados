package com.rasupermercados.rasupermercados.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class ArquivoUtils {

    public static Bitmap getBitmapFromFile(String fileName) {
        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/Download/" + fileName);

        return bitmap;
    }
}
