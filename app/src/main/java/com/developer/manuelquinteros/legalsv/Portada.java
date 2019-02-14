package com.developer.manuelquinteros.legalsv;

public class Portada {
    public String title;
    public String url;

    public Portada() {
    }

    public Portada(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
