package ru.x0xdc.otus.java;

import java.util.List;

public class AnyObject {

    private final int i;

    private final String test;

    private final int i1;

    private final List<Integer> list;

    public AnyObject(int i, String test, int i1, List<Integer> list) {
        this.i = i;
        this.i1 = i1;
        this.test = test;
        this.list = list;
    }

    public int getI() {
        return i;
    }

    public String getTest() {
        return test;
    }

    public int getI1() {
        return i1;
    }

    @Override
    public String toString() {
        return "AnyObject{" +
                "i=" + i +
                ", test='" + test + '\'' +
                ", i1=" + i1 +
                ", list=" + list +
                '}';
    }
}
