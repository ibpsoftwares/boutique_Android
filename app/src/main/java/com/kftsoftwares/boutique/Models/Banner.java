package com.kftsoftwares.boutique.Models;

/**
 * Created by apple on 04/05/18.
 */

public class Banner {

    String id , image ,name , category_id , stock_size;

    public String getStock_size() {
        return stock_size;
    }

    public void setStock_size(String stock_size) {
        this.stock_size = stock_size;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
