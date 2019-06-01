package com.instadp.profilepicture.finalfoodappserver.Model;

/**
 * Created by gaurav on 3/17/2018.
 */

public class Order {

    private String productId;
    private String produceName;
    private String quantity;
    private String price;
    private String discount;

    public Order() {

    }

    public Order(String productId, String produceName, String quantity, String price, String discount) {
        this.productId = productId;
        this.produceName = produceName;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProduceName() {
        return produceName;
    }

    public void setProduceName(String produceName) {
        this.produceName = produceName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
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
