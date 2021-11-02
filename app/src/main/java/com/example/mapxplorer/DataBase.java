package com.example.mapxplorer;

import java.util.ArrayList;

public class DataBase {
    public static ArrayList<Market> markets = new ArrayList<>();
    public static int id = 0;
    static {
        markets.add(new Market("Атб",49.23152796510891, 28.406867686183425)); // 0
        markets.add(new Market("А2б",49.23122796510891, 28.406867686183425));


        markets.get(0).getProducts().add(new Product("Молоко",20,2));
        markets.get(0).getProducts().add(new Product("Ковбаса",40,2));
        markets.get(0).getProducts().add(new Product("Сир",60,2));
        markets.get(0).getProducts().add(new Product("Пиво",22,2));
        markets.get(0).getProducts().add(new Product("Хліб",16,2));
        markets.get(0).getProducts().add(new Product("Риба",50,2));
        markets.get(0).getProducts().add(new Product("Шоколад",30,2));
        markets.get(0).getProducts().add(new Product("Вода Солока",15,2));
        markets.get(0).getProducts().add(new Product("Вода Газована",30,2));
        markets.get(0).getProducts().add(new Product("Вода Негазована",30,2));
        markets.get(0).getProducts().add(new Product("Сосиски молочні",30,2));
        markets.get(0).getProducts().add(new Product("Сосиски мисливські",30,2));
        markets.get(0).getProducts().add(new Product("Живчик 2Л",30,2));

    }
}
