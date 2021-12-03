package com.example.mapxplorer.Market;

import java.util.Objects;

public class Product implements Comparable<Product> {
    private String nameProduct;
    private double price;
    private int amount;
    private boolean isDiscount;

    public Product(){}

    public Product(String nameProduct,double price,int amount){
        this.nameProduct = nameProduct;
        this.price = price;
        this.amount = amount;
        isDiscount = false;
    }

    public String getNameProduct() { return nameProduct; }
    public void setNameProduct(String nameProduct) { this.nameProduct = nameProduct; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }
    public boolean isDiscount() { return isDiscount; }
    public void setDiscount(boolean discount) { isDiscount = discount; }

    public int compareTo(Product product) { // Comparable
        return Double.compare(this.price, product.price);
    }
    public int compareAmount(Product p){
        return Integer.parseInt(String.valueOf(Double.compare(amount, p.getAmount())));
    }
    public int compareName(Product p){
        return nameProduct.compareTo(p.nameProduct);
    }

    @Override
    public String toString() {
        return "\n\nProduct{" +
                "nameProduct='" + nameProduct + '\'' +
                ", price=" + price +
                ", amount=" + amount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(product.price, price) == 0 && amount == product.amount && isDiscount == product.isDiscount && Objects.equals(nameProduct, product.nameProduct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameProduct, price, amount, isDiscount);
    }
}
