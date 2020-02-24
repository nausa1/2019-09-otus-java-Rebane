package ru.otus.hw04;

import java.util.ArrayList;

public class MyClassImpl {

    @Log
    public static void static_method(String param, int param_int, long param_long, float param_float, double param_double, char param_char, byte param_byte, short param_short, ArrayList<Integer> param_arraylist) {
        System.out.println("STATIC METHOD");
    }

    @Log
    public void not_static_method(String param, int param_int, long param_long, float param_float, double param_double, char param_char, byte param_byte, short param_short, ArrayList<Integer> param_arraylist) {
        System.out.println("NOT STATIC METHOD");
    }

    public void not_logged_method(String param) {
        System.out.println("NOT LOGGED METHOD " + param);
    }
}
