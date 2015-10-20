package com.hammersmith.john.service.model;

/**
 * Created by Khmer on 9/14/2015.
 */
public class City  {
    private int id;
    private String name;
    private String image;
    public City(int id, String name,String image){
        this.setId(id);
        this.setName(name);
        this.setImage(image);
    }

    public City(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
