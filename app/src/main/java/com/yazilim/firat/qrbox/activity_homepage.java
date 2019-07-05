package com.yazilim.firat.qrbox;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuthActionCodeException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Scanner;

public class activity_homepage extends AppCompatActivity {

    public TextView textView;
    public static TextView dolapno_text;
    public static TextView platform_text;
    public static TextView dolap_id;
    public static TextView platform_id2;
    public static String scan_text;
    public static Button scan_btn,rent_btn,rent_cancel;
    private DatabaseReference ref;
    private DatabaseReference ref_dolapli;
    public static String ref_global;
    DatabaseReference kirala_ref;
    DatabaseReference giris_ref;


    public void success_Message(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("QR Box");
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setMessage("Dolap kiralama işleminiz başarılı..").setPositiveButton("Tamam", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void success_Cancel_Message(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("QR Box");
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setMessage("Dolap kiralama işleminiz başarı ile sonlandırıldı..").setPositiveButton("Tamam", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent giris_Intent = getIntent();
        final String giris_mail = giris_Intent.getStringExtra("giris_mail");

        TextView mail = findViewById(R.id.giris_mail);
        mail.setText(giris_mail);
        final String giris_no[] = giris_mail.split("@");


        platform_id2 = (TextView)findViewById(R.id.platform_id);
        CharSequence plat_id = platform_id2.getText();
        kirala_ref = FirebaseDatabase.getInstance().getReference().child("dolaplar/5eb03d464081f6131395803b92da60e8");

        ////////////////// QR CODE SCAN  ///////////////////

        dolap_id = (TextView)findViewById(R.id.dolap_id);
        dolapno_text = (TextView)findViewById(R.id.result_text);
        platform_text = (TextView)findViewById(R.id.dolap_text);
        scan_btn = (Button)findViewById(R.id.btn_qrscan);
        rent_btn = (Button)findViewById(R.id.btn_rent);
        rent_cancel = (Button)findViewById(R.id.btn_rent_delete);

        rent_btn.setEnabled(false);
        rent_btn.setBackgroundResource(R.drawable.btn_bg_disable);
        rent_btn.setTextColor(Color.parseColor("#AAAAAA"));

        rent_cancel.setEnabled(false);
        rent_cancel.setBackgroundResource(R.drawable.btn_bg_disable);
        rent_cancel.setTextColor(Color.parseColor("#AAAAAA"));

        rent_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Time time = new Time();
                time.setToNow();
                int hour = time.hour;
                int minute = time.minute;
                int monthDay = time.monthDay;
                int month = (time.month)+1;
                int year = time.year;
                String open_time = hour + ":" + minute + " " + monthDay+"/"+month+"/"+year;

                final String durum = "false";
                final String kiralayan = giris_no[0];
                final String saat = open_time;
                CharSequence id = dolap_id.getText();
                kirala_ref.child("dolap00"+id).setValue(new dolap(durum,kiralayan,saat));

                scan_btn.setEnabled(false);
                scan_btn.setBackgroundResource(R.drawable.btn_bg_disable);
                scan_btn.setTextColor(Color.parseColor("#AAAAAA"));

                rent_btn.setEnabled(false);
                rent_btn.setBackgroundResource(R.drawable.btn_bg_disable);
                rent_btn.setTextColor(Color.parseColor("#AAAAAA"));

                rent_cancel.setEnabled(true);
                rent_cancel.setBackgroundResource(R.drawable.btn_bg3);
                rent_cancel.setTextColor(Color.parseColor("#FFFFFF"));

                platform_text.setText("Kiralanan Dolap No: "+id+"\n\n"+"Açılış Zamanı: "+open_time);
                success_Message();
            }
        });

        rent_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String durum = "true";
                final String kiralayan = "null";
                final String saat = "null";
                CharSequence id = dolap_id.getText();
                kirala_ref.child("dolap00"+id).setValue(new dolap(durum,kiralayan,saat));

                scan_btn.setEnabled(true);
                scan_btn.setBackgroundResource(R.drawable.btn_bg);
                scan_btn.setTextColor(Color.parseColor("#FFFFFF"));

                rent_btn.setEnabled(false);
                rent_btn.setBackgroundResource(R.drawable.btn_bg_disable);
                rent_btn.setTextColor(Color.parseColor("#AAAAAA"));

                rent_cancel.setEnabled(false);
                rent_cancel.setBackgroundResource(R.drawable.btn_bg_disable);
                rent_cancel.setTextColor(Color.parseColor("#AAAAAA"));

                platform_text.setText("Platform Mesaj");
                success_Cancel_Message();
            }
        });


        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),activity_scancode.class));
            }
        });

        ////////////////////////////////////////////////////

        Button button = (Button) findViewById(R.id.btn_back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBackActivity();
            }
        });
    }
    public void openBackActivity(){
        this.finish();
    }
}
