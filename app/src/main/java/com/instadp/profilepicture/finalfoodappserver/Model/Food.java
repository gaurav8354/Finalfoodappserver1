package com.instadp.profilepicture.finalfoodappserver.Model;

/**
 * Created by gaurav on 3/12/2018.
 */

public class Food {

    private String description,image,menuId,name,price,discount;

    public Food() {
    }

    public Food(String description, String image, String menuId, String name, String price,String discount) {
        this.description = description;
        this.image =image;
        this.menuId = menuId;
        this.name = name;
        this.price = price;
        this.discount=discount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {

        this.discount = discount;
    }
}
