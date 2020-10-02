package ru.x0xdc.otus.java;

import com.google.common.base.CharMatcher;

public class HelloOtus {

    public static void main(String... args) {
        var unformatted = "  \tText  \u2009 stripped of all unwanted  spaces   ";

        System.out.println(CharMatcher.whitespace().trimAndCollapseFrom(unformatted, ' '));
    }
}
