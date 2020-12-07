package ru.x0xdc.otus.java;

import ru.x0xdc.otus.java.json.MyGson;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        MyGson myGson = new MyGson();
        AnyObject obj = new AnyObject(22, "_test_", 10, List.of(10, 20, 30));
        String myJson = myGson.toJson(obj);

        System.out.println("Object: " + obj + "\nSerialized: " + myJson);
    }
}

