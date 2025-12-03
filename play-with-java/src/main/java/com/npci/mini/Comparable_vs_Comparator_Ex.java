package com.npci.mini;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// Author : Nag
class Product implements Comparable<Product> {
    int id;
    String name;
    float price;

    public Product(int id, String name, float price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    @Override
    public int compareTo(Product o) {
        return this.id - o.id;
    }

    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + name + ", price=" + price + "]";
    }
}


public class Comparable_vs_Comparator_Ex {
    public static void main(String[] args) {

        List<Integer> integers = new ArrayList<>();
        integers.add(5);
        integers.add(1);
        integers.add(3);
        Collections.sort(integers);

        Product p1 = new Product(102, "Laptop", 45000f);
        Product p2 = new Product(101, "Mouse", 1500f);
        Product p3 = new Product(103, "Keyboard", 2500f);

        List<Product> products = new ArrayList<>();
        products.add(p1);
        products.add(p2);
        products.add(p3);

        // Function aka Lambda Expression
        Comparator<Product> byId = (item1, item2) -> item1.id - item2.id;
        Comparator<Product> byPrice = (item1, item2) -> Float.compare(item1.price, item2.price);
        Comparator<Product> byIdAndThenPrice = byId.thenComparing(byPrice); // Function composition

        Collections.sort(products);
    }

    private static void displayProducts(List<Product> products) {
        for (Product p : products) {
            System.out.println(p);
        }
    }
}

