package com.rasupermercados.rasupermercados.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rasupermercados.rasupermercados.R;

import java.io.IOException;
import java.io.InputStream;

public class BannerFragment extends DialogFragment {

    private ImageView ivBanner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_banner, container, false);

        ImageView imageView = view.findViewById(R.id.iv_banner);
        String nomeImagem = getArguments().getString("nome_imagem");
        try {
            InputStream ims = getActivity().getAssets().open(nomeImagem);
            Drawable d = Drawable.createFromStream(ims, null);
            imageView.setImageDrawable(d);
        }
        catch(IOException ex) {
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

}
