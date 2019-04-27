package com.instadp.profilepicture.finalfoodappserver.Model;

/**
 * Created by gaurav on 3/17/2018.
 */

public class Order {

    private String ProductId;
    private String ProduceName;
    private String Quantity;
    private String Price;
    private String Discount;

    public Order() {

    }

    public Order(String productId, String produceName, String quantity, String price, String discount) {
        ProductId = productId;
        ProduceName = produceName;
        Quantity = quantity;
        Price = price;
        Discount = discount;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProduceName() {
        return ProduceName;
    }

    public void setProduceName(String produceName) {
        ProduceName = produceName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}
