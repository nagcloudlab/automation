package com.example.mini_examples.p2;

import com.example.mini_examples.p1.A;

public class E {

    public void eObjectMethod() {
        System.out.println("E's method");
        A a = new A();
        // System.out.println("pri: " + a.pri); // private member of A
        // System.out.println("def: " + a.def); // default member of A - not accessible
        // outside package
        // System.out.println("pro: " + a.pro); // protected member of A - not
        // accessible outside package
        System.out.println("pub: " + a.pub); // public member of A - accessible everywhere
    }

}
