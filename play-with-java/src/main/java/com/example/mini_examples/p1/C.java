package com.example.mini_examples.p1;

public class C {

    public void cObjectMethod() {
        System.out.println("C's method");
        A a = new A();
        // System.out.println("pri: " + a.pri); // private member of A
        System.out.println("def: " + a.def); // default member of A
        System.out.println("pro: " + a.pro); // protected member of A
        System.out.println("pub: " + a.pub); // public member of A
    }

}
