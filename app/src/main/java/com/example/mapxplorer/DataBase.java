package com.example.mapxplorer;

import java.util.ArrayList;

public class DataBase {
    public static ArrayList<Market> markets = new ArrayList<>();
    static {
        markets.add(new Market("Атб",49.23152796510891, 28.406867686183425));
        markets.add(new Market("Магазин",49.23114320346306, 28.406530458796375));
        markets.add(new Market("Абт",49.23164320346306, 28.406530458796375));
    }
}
