package com.developer.manuelquinteros.legalsv.Model;

public class Menu {
    public int id;
    public String title;
    private String subtitle;
    public String url;

    public Menu() {
    }

    public Menu(int id, String title, String subtitle, String url) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
