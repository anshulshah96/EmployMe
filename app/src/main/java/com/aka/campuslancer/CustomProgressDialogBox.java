package com.aka.campuslancer;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;


public class CustomProgressDialogBox extends ProgressDialog {

    public TextView messagetv;
    public AnimationDrawable animation;
    private CharSequence dialogMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_progress_dialog_box);

        ImageView logo = (ImageView) findViewById(R.id.progressLogo);
        logo.setBackgroundResource(R.drawable.custom_progress_dialog_animation);
        animation = (AnimationDrawable) logo.getBackground();
        messagetv = (TextView) findViewById(R.id.message);
        messagetv.setText(dialogMessage);
    }

    public CustomProgressDialogBox(Context context, CharSequence dialogMessage) {
        super(context);
//        this.context
        this.dialogMessage = dialogMessage;
    }

    @Override
    public void show() {
        super.show();
        animation.start();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        animation.stop();
    }

}