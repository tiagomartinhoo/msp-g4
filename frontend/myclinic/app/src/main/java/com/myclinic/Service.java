package com.myclinic;

public class Service {
    private String id;
    private String name;
    private String price;

    public Service(String id, String name, String price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return name;
    }
}
