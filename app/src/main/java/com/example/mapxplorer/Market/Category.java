package com.example.mapxplorer.Market;

import java.util.ArrayList;

public class Category {
    private ArrayList<Product> products;
    private String nameCategory;
    public Category(){ products = new ArrayList<>(); }
    public String getNameCategory() { return nameCategory; }
    public void setNameCategory(String nameCategory) { this.nameCategory = nameCategory; }
    public ArrayList<Product> getProducts() { return products; }
    public void setProducts(ArrayList<Product> products) { this.products = products; }
}
