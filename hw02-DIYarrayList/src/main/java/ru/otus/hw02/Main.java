package ru.otus.hw02;

import java.util.Collections;

public class Main {
    public static void main(String[] args) {
        DIYarrayList <Integer> testList = new DIYarrayList <>();
        DIYarrayList <Number> testList_new = new DIYarrayList <>();
        Collections.addAll(testList, 1, 2, 3, 4, -5, 6 -7, 8, 10, -100, -20, -100, 1000, 12, 13, -14, 15, 16, -17);
        System.out.println("testList:");
        testList.print();

        Collections.addAll(testList_new, 0.1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        System.out.println("testList_new:");
        testList_new.print();

        Collections.copy(testList_new, testList);
        System.out.println("testList_new after copy:");
        testList_new.print();

        Collections.sort(testList);
        System.out.println("testList after sort:");
        testList.print();
    }
}
