package com.example.mapxplorer;

public class Product implements Comparable<Product> {
    private String nameProduct;
    private double price;
    private int amount;

    public Product(){}

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
}
