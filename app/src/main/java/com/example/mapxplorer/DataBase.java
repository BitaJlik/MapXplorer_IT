package com.example.mapxplorer;

import java.util.ArrayList;

public class DataBase {
    public static ArrayList<Market> markets = new ArrayList<>();
    public static int id = 0;
    static {
        markets.add(new Market("Атб",49.23152796510891, 28.406867686183425)); // 0
        markets.add(new Market("Магазинчик",49.2358082095236, 28.408807136430234));
        markets.add(new Market("Магазин",49.23471943809688, 28.41317511598384));
        markets.add(new Market("Імперія м'яса",49.23092745354353, 28.405822843672837));

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

        markets.get(1).getProducts().add(new Product("Шоколад",30,26));
        markets.get(1).getProducts().add(new Product("Вода Солока",15,24));
        markets.get(1).getProducts().add(new Product("Вода Газована",30,23));
        markets.get(1).getProducts().add(new Product("Вода Негазована",30,22));
        markets.get(1).getProducts().add(new Product("Сосиски молочні",30,22));
        markets.get(1).getProducts().add(new Product("Сосиски мисливські",30,22));
        markets.get(1).getProducts().add(new Product("Живчик 2Л",30,21));
        markets.get(1).getProducts().add(new Product("Сир",60,22));
        markets.get(1).getProducts().add(new Product("Молоко",20,4));
        markets.get(1).getProducts().add(new Product("Пиво",22,21));
        markets.get(1).getProducts().add(new Product("Хліб",16,2));        
        markets.get(1).getProducts().add(new Product("Риба",50,2));

        markets.get(3).getProducts().add(new Product("Філе",120,5));








    }
}
