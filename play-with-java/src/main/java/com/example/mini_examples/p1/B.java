package com.example.mini_examples.p1;

public class B extends A {

    public void bObjectMethod() {
        System.out.println("B's method");
        // System.out.println("pri: " + pri); // private member of A - not accessible
        System.out.println("def: " + def); // default member of A - accessible within same package
        System.out.println("pro: " + pro); // protected member of A - accessible within same package
        System.out.println("pub: " + pub); // public member of A - accessible everywhere
    }

}
