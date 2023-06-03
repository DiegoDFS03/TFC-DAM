package com.example.gamesaverx.gamesaverx.Utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public class Offer implements Serializable {
    private String title,store,image,discount_percentage,original_price,end_date;

    public Offer() {

    }


    public String getTitle() {
        return title;
    }

    public String getStore() {
        return store;
    }

    public String getImage() {
        return image;
    }

    public String getDiscount_percentage() {
        return discount_percentage;
    }

    public String getOriginal_price() {
        return original_price;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDiscount_percentage(String discount_percentage) {
        this.discount_percentage = discount_percentage;
    }

    public void setOriginal_price(String original_price) {
        this.original_price = original_price;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public Offer(String title, String store, String image, String discount_percentage, String original_price, String end_date) {
        this.title = title;
        this.store = store;
        this.image = image;
        this.discount_percentage = discount_percentage;
        this.original_price = original_price;
        this.end_date = end_date;
    }
}




