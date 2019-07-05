package com.yazilim.firat.qrbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_info extends AppCompatActivity {

    private DatabaseReference ref;
    private DatabaseReference ref_dolapli;
    public static String ref_global;
    private Button button;

    String dolap_key;
    String dolapno_text;
    String durum = "true";
    String kiralayan = "null";
    String saat = "null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent scan_Intent = getIntent();
        dolapno_text = scan_Intent.getStringExtra("dolapno_text");

        ref_global = ref_dolapli.toString();

        ref = FirebaseDatabase.getInstance().getReference().child("platformlar");
        dolap_key = dolapno_text;
        ref_dolapli = ref.child(dolap_key);

        ref_dolapli.child("dolap001").setValue(new dolap(durum,kiralayan,saat));
        ref_dolapli.child("dolap002").setValue(new dolap(durum,kiralayan,saat));


        button = (Button) findViewById(R.id.btn_back);
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