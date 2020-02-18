package ru.otus.hw04;

import java.util.ArrayList;

/*
    java -javaagent:proxyDemo.jar -jar proxyDemo.jar - для запуска в папке target
*/
public class ProxyDemo {

    public static void main(String[] args) {
        MyClassImpl myClass = new MyClassImpl();
        ArrayList<Integer> arr= new ArrayList<>();
        arr.add(0);
        arr.add(1);
        new MyClassImpl().static_method("String param static", 3, 12345678910l, 0.7f, 2.5678d, 'c', (byte) 10, (short) 1, arr);
        myClass.not_logged_method("not logged method param");
        myClass.not_static_method("String param not static", 1, 99999999999l, 0.75f, 3.8935d, 's', (byte) 15, (short) 30, arr);
    }

}
