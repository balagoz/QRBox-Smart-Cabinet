package com.yazilim.firat.qrbox;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

public class loading extends AppCompatActivity {

    //private static int SPLASH_TIME_OUT = 3000;
    private Button button;

    RelativeLayout rellay1,rellay2;

    Handler handler = new Handler();
    Runnable runnable =  new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };


    public boolean isOnline()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }

    public void connectionMessage()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("QR Box");
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setMessage("Lütfen internet bağlantınızı kontrol ediniz..").setPositiveButton("Tamam", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                finish();
            }
        });

        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        if(!isOnline())
        {
            connectionMessage();
        }
        else {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

           /* new Handler().postDelayed(new Runnable(){
                @Override
                public void run(){
                    Intent homeIntent = new Intent(loading.this, login.class);
                    startActivity(homeIntent);
                    finish();
                }
            },SPLASH_TIME_OUT);*/

            rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
            rellay2 = (RelativeLayout) findViewById(R.id.rellay2);

            handler.postDelayed(runnable,2000);

            button = (Button) findViewById(R.id.button_signup);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openActivity_signup();
                }
            });

            button = (Button) findViewById(R.id.button_sifremiUnuttum);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {openActivity_sifremiUnuttum();
                }
            });
        }
    }

    public void openActivity_signup(){
        Intent intent = new Intent(this, activity_signup.class);
        startActivity(intent);
    }

    public void openActivity_sifremiUnuttum(){
        Intent intent = new Intent(this, activity_sifremiUnuttum.class);
        startActivity(intent);
    }
}
