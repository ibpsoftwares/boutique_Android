package com.kftsoftwares.boutique.Models;

import java.util.List;

/**
 * Created by apple on 22/02/18.
 */

public class SingleProduct {

    private String id;
    private String categoryId;
    private String categoryName;
    private String title;
    private String description;
    private String price;
    private String colour;
    private String deleteStatus;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(String deleteStatus) {
        this.deleteStatus = deleteStatus;
    }


}
