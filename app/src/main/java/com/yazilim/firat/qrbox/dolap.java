package com.yazilim.firat.qrbox;

public class dolap {

    private String durum;
    private String kiralayan;
    private String saat;

    public dolap(){}

    public dolap(String durum, String kiralayan, String saat) {
        this.durum = durum;
        this.kiralayan = kiralayan;
        this.saat = saat;
    }

    public String getDurum() {
        return durum;
    }

    public void setDurum(String durum) {
        this.durum = durum;
    }

    public String getKiralayan() {
        return kiralayan;
    }

    public void setKiralayan(String kiralayan) {
        this.kiralayan = kiralayan;
    }

    public String getSaat() {
        return saat;
    }

    public void setSaat(String saat) {
        this.saat = saat;
    }
}