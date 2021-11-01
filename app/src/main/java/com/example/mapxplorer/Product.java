package com.example.mapxplorer;

public class Product {
    private String nameProduct;
    private double price;
    private int amount;

    public Product(String nameProduct,double price,int amount){
        this.nameProduct = nameProduct;
        this.price = price;
        this.amount = amount;
    }

    public String getNameProduct() { return nameProduct; }
    public void setNameProduct(String nameProduct) { this.nameProduct = nameProduct; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }
}
