package com.yazilim.firat.qrbox;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaCas;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class activity_mail_verification extends AppCompatActivity {

    int myProgress = 0;
    ProgressBar progressBarView;
    TextView tv_time;
    int et_timer = 180;
    int progress;
    CountDownTimer countDownTimer;
    int endTime = 250;
    TextView textView;
    String dogrulama_kodu = "654123";
    EditText verification_code;
    Button verification_button;


    Session session = null;
    ProgressDialog pdialog = null;
    Context context = null;
    String rec, subject, textMessage;

    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_verification);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        db = FirebaseDatabase.getInstance();

        Intent myIntent = getIntent();
        final String ogrenci_no = myIntent.getStringExtra("ogrenci_noText");
        final String ogrenci_mail = myIntent.getStringExtra("ogrenci_mailText");
        final String parola = myIntent.getStringExtra("parolaText");
        //String lName = myIntent.getStringExtra("lastName");


        /*textView2 = (TextView) findViewById(R.id.dogrulama_text);
        textView2.setText(ogrenci_mail);
        */
        //Mail Gönderme

        context = this;

        rec = ogrenci_mail;
        subject = "QR Code Onay Şifresi";
        textMessage = "Mail İçeriği Onay Kodu : " + dogrulama_kodu;


        Properties props = new Properties();
        props.put("mail.smtp.host","smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port","465");
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.port","465");

        session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("burakhanalagoz@gmail.com","Jarvis.1976");
            }
        });

        pdialog = ProgressDialog.show(context,"","Okul mailinize onay kodu gönderiliyor..",true);

        RetreiveFeedTask task = new RetreiveFeedTask();
        task.execute();


        /*String hedef_mail = "15290052@firat.edu.tr" ;
        String mail_baslik = "QR Code Onay Şifresi" ;
        String mail_icerik = "Mail İçeriği Onay Kodu : 123456";

        Intent intent2 = new Intent(Intent.ACTION_SEND);
        intent2.putExtra(Intent.EXTRA_EMAIL, new String[]{hedef_mail});
        intent2.putExtra(Intent.EXTRA_SUBJECT, mail_baslik);
        intent2.putExtra(Intent.EXTRA_TEXT, mail_icerik);
        */
        /*
        Random r = new Random();
        int dogrulama_kodu = r.nextInt(987654 - 234567) + 234567;

        textView2 = (TextView) findViewById(R.id.dogrulama_text);
        textView2.setText(dogrulama_kodu);
        */
        //

        progressBarView = (ProgressBar) findViewById(R.id.view_progress_bar);
        tv_time = (TextView)findViewById(R.id.tv_timer);

        /*Geri Sayım Animasyonu*/
        RotateAnimation makeVertical = new RotateAnimation(0, -90, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        makeVertical.setFillAfter(true);
        progressBarView.startAnimation(makeVertical);
        progressBarView.setSecondaryProgress(endTime);
        progressBarView.setProgress(0);

        fn_countdown();

        verification_code = (EditText)findViewById(R.id.verification_code);
        verification_button = (Button) findViewById(R.id.verification_button);


        verification_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if ((verification_code.getText().toString()).equals(dogrulama_kodu)){

                    DatabaseReference dbRef = db.getReference("kullanicilar");
                    String key = dbRef.push().getKey();
                    DatabaseReference dbRefKeyli = db.getReference("kullanicilar/"+key);

                    dbRefKeyli.setValue(new Kullanici(ogrenci_no,ogrenci_mail,parola));

                    AlertDialog.Builder builder = new AlertDialog.Builder(activity_mail_verification.this);
                    builder.setTitle("QR Box");
                    builder.setMessage("Kaydınız Başarılı, Giriş Ekranına Yönlendiriliyorsunuz..");
                    builder.setIcon(R.mipmap.ic_launcher_round);
                    builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            activity_mail_verification.this.finish();
                        }
                    });
                    builder.show();


                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity_mail_verification.this);
                    builder.setTitle("QR Box");
                    builder.setMessage("Girdiğiniz şifre hatalı, lütfen daha sonra tekrar deneyiniz..");
                    builder.setIcon(R.mipmap.ic_launcher_round);
                    builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            activity_mail_verification.this.finish();
                        }
                    });
                    builder.show();
                }

            }
        });


    }

    class RetreiveFeedTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params){
            try{
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("burakhanalagoz@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(rec));
                message.setSubject(subject);
                message.setContent(textMessage,"text/html; charset=utf-8");

                Transport.send(message);
            }
            catch (MessagingException e){
                e.printStackTrace();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result){

            pdialog.dismiss();
            Toast.makeText(getApplicationContext(),"Mail Gönderildi",Toast.LENGTH_LONG).show();

        }
    }

    private void fn_countdown() {

        if (et_timer > 0) {
            myProgress = 0;

            try {
                countDownTimer.cancel();

            } catch (Exception e) {

            }

            int timeInterval = et_timer;
            progress = 1;
            endTime = timeInterval; // up to finish time

            countDownTimer = new CountDownTimer(endTime * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    setProgress(progress, endTime);
                    progress = progress + 1;
                    int seconds = (int) (millisUntilFinished / 1000) % 60;
                    int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                    String newtime = minutes + ":" + seconds;

                    if (newtime.equals("0:0")) {
                        tv_time.setText("00:00");
                    } else if ((String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                        tv_time.setText("0" + minutes + ":0" + seconds);
                    } else if ((String.valueOf(minutes).length() == 1)) {
                        tv_time.setText("0" + minutes + ":" + seconds);
                    } else if ((String.valueOf(seconds).length() == 1)) {
                        tv_time.setText("0" + minutes + ":0" + seconds);
                    } else if ((String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                        tv_time.setText("0" + minutes + ":0" + seconds);
                    } else if (String.valueOf(minutes).length() == 1) {
                        tv_time.setText(minutes + ":" + seconds);
                    } else if (String.valueOf(seconds).length() == 1) {
                        tv_time.setText(minutes + ":0" + seconds);
                    } else {
                        tv_time.setText(minutes + ":" + seconds);
                    }

                }

                @Override
                public void onFinish() {
                    setProgress(progress, endTime);
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity_mail_verification.this);
                    builder.setTitle("QR Box");
                    builder.setIcon(R.mipmap.ic_launcher_round);
                    builder.setMessage("Onay giriş süresi doldu. Lütfen daha sonra tekrar deneyiniz..").setPositiveButton("Tamam", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            finish();
                        }
                    });

                    final AlertDialog alert = builder.create();
                    alert.show();
                    //activity_mail_verification.this.finish();
                }
            };
            countDownTimer.start();
        }else {
            //Toast.makeText(getApplicationContext(),"Please enter the value",Toast.LENGTH_LONG).show();
        }
    }

    public void setProgress(int startTime, int endTime) {
        progressBarView.setMax(endTime);
        progressBarView.setSecondaryProgress(endTime);
        progressBarView.setProgress(startTime);
    }
}
