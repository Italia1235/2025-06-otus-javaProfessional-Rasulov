package ru.rasulov;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class HelloOtus {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("Joe");
        list.add("Carl");
        list.add("Ron");


        System.out.println(Lists.reverse(list));
    }
}