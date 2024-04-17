package com.example.myclinic.Models;

public class MainListModel {

    int image;
    String id, name, address, price, disp;

    public MainListModel(int image, String id, String name, String address, String price, String disp) {
        this.image = image;
        this.id = id;
        this.name = name;
        this.address = address;
        this.price = price;
        this.disp = disp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDisp() {
        return disp;
    }

    public void setDisp(String disp) {
        this.disp = disp;
    }
}
