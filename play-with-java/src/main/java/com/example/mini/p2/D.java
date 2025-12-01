package com.example.mini.p2;

import com.example.mini.p1.A;

public class D extends A {

    public void dObjectMethod() {
        System.out.println("D's method");
        // System.out.println("pri: " + pri); // private member of A - not accessible
        // System.out.println("def: " + def); // default member of A - not accessible
        // outside package
        System.out.println("pro: " + pro); // protected member of A - accessible in subclass
        System.out.println("pub: " + pub); // public member of A - accessible everywhere
    }

}
