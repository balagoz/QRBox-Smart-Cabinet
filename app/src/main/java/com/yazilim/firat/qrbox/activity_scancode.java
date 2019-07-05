package com.yazilim.firat.qrbox;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import java.util.Scanner;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.yazilim.firat.qrbox.activity_homepage.dolap_id;
import static com.yazilim.firat.qrbox.activity_homepage.rent_btn;
import static com.yazilim.firat.qrbox.activity_homepage.rent_cancel;

public class activity_scancode extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView scannerView;
    String text;
    private DatabaseReference ref;
    private DatabaseReference ref2;
    int full = 0;
    int free = 0;
    int destroy = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
    }

    @Override
    public void handleResult(Result result) {

        ref = FirebaseDatabase.getInstance().getReference().child("dolaplar");
        /*if (!ref.child(scan_code).equals(null)) {
            activity_homepage.dolapno_text.setText("Doğru QR Kod..");
        }
        else{
            activity_homepage.dolapno_text.setText("Hatalı QR Kod..");
        }*/

        if (result.getText().equals("5eb03d464081f6131395803b92da60e8")) {
            activity_homepage.dolapno_text.setText("Fırat Üni. Yazılım Müh.");
            final String ref_dolap = result.getText();

            ref2 = FirebaseDatabase.getInstance().getReference().child("dolaplar/"+ref_dolap);

            int i;
            for (i = 1 ; i < 3 ; i++) {
                /*if (destroy == 1){
                    break;
                }*/
                final int bos_dolap = i;
                ref2.child("dolap00"+i).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //dolap durum_data = dataSnapshot.getValue();
                        String durum_data = dataSnapshot.child("durum").getValue(String.class);
                        if (durum_data.equals("false")){
                            full = full+1;
                        }
                        if (durum_data.equals("true")) {
                            free = free+1;
                            activity_homepage.platform_text.setText(bos_dolap +" Numaralı Dolap Boş..");
                            //destroy = 1;

                            rent_btn.setEnabled(true);
                            rent_btn.setBackgroundResource(R.drawable.btn_bg2);
                            rent_btn.setTextColor(Color.parseColor("#FFFFFF"));

                            activity_homepage.dolap_id.setText(""+bos_dolap);
                            activity_homepage.platform_id2.setText(""+ref_dolap);

                        }
                        if (free==0){
                            activity_homepage.platform_text.setText("Tüm Dolaplar Dolu..");
                            rent_btn.setEnabled(false);
                            rent_btn.setBackgroundResource(R.drawable.btn_bg_disable);
                            rent_btn.setTextColor(Color.parseColor("#AAAAAA"));
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

           /*if(free>0)
           {
               //String yazi = "Dolu dolap:"+dolu_dolap + " Bos dolap:" + bos_dolap;
               activity_homepage.platform_text.setText("Tüm dolaplar dolu.." + free + full);
           }
           else
           {
               activity_homepage.platform_text.setText("Boş dolaplar var.." + free + full);
           }*/
        }
        else{
            activity_homepage.dolapno_text.setText("Hatalı QR Kod");
            activity_homepage.platform_text.setText("Hatalı bir QR Kod okuttunuz. Lütfen geçerli bir kod okutmayı deneyiniz..");
        }

        /*
        activity_homepage.dolapno_text.setText(result.getText());
        activity_homepage.scan_text = result.getText();
        */

        /*
        text = result.getText();
        Intent scan_Intent = new Intent(activity_scancode.this, activity_info.class);
        scan_Intent.putExtra("dolapno_text",text);
        startActivity(scan_Intent);
        */

        onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
}
