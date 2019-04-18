package com.yazilim.firat.qrbox;

public class Kullanici {

    private String ogrenci_no,ogrenci_mail,parola;

    public Kullanici(){}

    public Kullanici(String ogrenci_no, String ogrenci_mail, String parola) {
        this.ogrenci_no = ogrenci_no;
        this.ogrenci_mail = ogrenci_mail;
        this.parola = parola;
    }

    public String getNo() {
        return ogrenci_no;
    }

    public void setNo(String ogrenci_no) {
        this.ogrenci_no = ogrenci_no;
    }

    public String getMail() {
        return ogrenci_mail;
    }

    public void setMail(String ogrenci_mail) {
        this.ogrenci_mail = ogrenci_mail;
    }

    public String getPass() {
        return parola;
    }

    public void setPass(String parola) {
        this.parola = parola;
    }
}