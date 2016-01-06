package com.harkanson.android.resume;

import android.databinding.BindingAdapter;
import android.graphics.Typeface;
import android.widget.TextView;

public class Bindings {

    @BindingAdapter({"bind:font"})
    public static void setFont(TextView textView, String fontName) {
        textView.setTypeface(Typeface.createFromAsset(textView.getContext().getAssets(), "fonts/" + fontName));
    }

}
