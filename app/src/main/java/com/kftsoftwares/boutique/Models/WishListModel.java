package com.kftsoftwares.boutique.Models;

/**
 * Created by apple on 22/02/18.
 */

public class WishListModel {

  private   String wishListID,
            ClothId,
            catrgoryId,
            title,
            descreption,
            price,
            colour,
            delete_status,
            currency,image1,offerprice,brand;

    public String getOfferprice() {
        return offerprice;
    }

    public void setOfferprice(String offerprice) {
        this.offerprice = offerprice;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getWishListID() {
        return wishListID;
    }

    public void setWishListID(String wishListID) {
        this.wishListID = wishListID;
    }

    public String getClothId() {
        return ClothId;
    }

    public void setClothId(String clothId) {
        ClothId = clothId;
    }

    public String getCatrgoryId() {
        return catrgoryId;
    }

    public void setCatrgoryId(String catrgoryId) {
        this.catrgoryId = catrgoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescreption() {
        return descreption;
    }

    public void setDescreption(String descreption) {
        this.descreption = descreption;
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

    public String getDelete_status() {
        return delete_status;
    }

    public void setDelete_status(String delete_status) {
        this.delete_status = delete_status;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }
}
