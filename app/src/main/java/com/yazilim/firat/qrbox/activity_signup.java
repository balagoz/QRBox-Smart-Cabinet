package com.yazilim.firat.qrbox;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class activity_signup extends AppCompatActivity {
    private Button button,button_kayit;
    private EditText parola,parola_tekrar,ogrenci_no,ogrenci_mail;
    private TextView label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        button = (Button) findViewById(R.id.btn_back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBackActivity();
            }
        });

        Spinner dropdown = findViewById(R.id.okul_adi);
        String[] items = new String[]{"Fırat Üniversitesi", "Boğaziçi Üniversitesi", "Orta Doğu Teknik Üniversitesi", "A","B","C","D","E","F","G","H"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);


        button_kayit = (Button) findViewById(R.id.btn_kayit);
        button_kayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ogrenci_no = (EditText) findViewById(R.id.ogrenci_no);
                ogrenci_mail = (EditText) findViewById(R.id.ogrenci_mail);
                parola = (EditText) findViewById(R.id.parola);
                parola_tekrar = (EditText) findViewById(R.id.parola_tekrar);
                label = (TextView) findViewById(R.id.label);

                String ogrenci_noText = ogrenci_no.getText().toString();
                String ogrenci_mailText = ogrenci_mail.getText().toString();
                String parolaText = parola.getText().toString();
                String parolaTekrarText = parola_tekrar.getText().toString();

                //label.setText(ogrenci_noText + ogrenci_mailText + parolaText + parolaTekrarText );
                if (ogrenci_noText.isEmpty()){
                    setError(ogrenci_no,"* işaretli alanlar boş geçilemez..");
                }
                if(ogrenci_mailText.isEmpty()){
                    setError(ogrenci_mail,"* işaretli alanlar boş geçilemez..");
                }
                if (parolaText.isEmpty()){
                    setError(parola,"* işaretli alanlar boş geçilemez..");
                }
                if (parolaTekrarText.isEmpty()){
                    setError(parola_tekrar,"* işaretli alanlar boş geçilemez..");
                }

                if (parola.length()< 6){
                    setError(parola,"Şifre 6 karakterden kısa olamaz..");
                }

                if (parola_tekrar.length()< 6){
                    setError(parola_tekrar,"Şifre 6 karakterden kısa olamaz..");
                }

                if (!parolaText.equals(parolaTekrarText)){
                    setError(parola_tekrar,"İki şifre aynı değil..");
                }
                /*
                else{
                    label.setText("Kayıt Başarılı Gibi.. :D");
                }
                */

                if(ogrenci_no.getError() != null || ogrenci_mail.getError() != null || parola.getError() != null || parola_tekrar.getError() != null){

                }
                else{

                    //label.setText("Kayıt Başarılı Gibi.. :D");

                    /*AlertDialog.Builder builder = new AlertDialog.Builder(activity_signup.this);
                    builder.setTitle("QR Box");
                    builder.setMessage("Kayıt Başarılı Gibi.. :D");
                    builder.setIcon(R.mipmap.ic_launcher_round);
                    builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
                    */

                    Intent myIntent = new Intent(activity_signup.this, activity_mail_verification.class);
                    myIntent.putExtra("ogrenci_noText", ogrenci_noText);
                    myIntent.putExtra("ogrenci_mailText", ogrenci_mailText);
                    myIntent.putExtra("parolaText", parolaText);
                    //myIntent.putExtra("lastName", "Your Last Name Here");
                    startActivity(myIntent);
                    activity_signup.this.finish();

                }
            }
        });
    }

    public void openBackActivity(){
        this.finish();
    }

    public static boolean isEmpty(EditText editText) {

        String input = editText.getText().toString().trim();
        return input.length() == 0;

    }
    public static void setError(EditText editText, String errorString) {

        editText.setError(errorString);

    }

    public static void clearError(EditText editText) {

        editText.setError(null);

    }
}
