package com.kftsoftwares.boutique.Models;

/**
 * Created by apple on 21/02/18.
 */

public class GetAllProductModel {

        private String id;
        private String categoryId;
        private String categoryName;
        private String title;
        private String description;
        private String price;
        private String offerprice;
        private String colour;
        private String deleteStatus;
        private String currency;
        private String image1;
        private String brandName;
        private String Wish_list;


    public String getWish_list() {
        return Wish_list;
    }

    public void setWish_list(String wish_list) {
        Wish_list = wish_list;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

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
   public String getOfferPrice() {
            return offerprice;
        }

        public void setOfferPrice(String price) {
            this.offerprice = price;
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

