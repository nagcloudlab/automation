package com.example.mini_examples;

import com.example.mini_examples.p1.A;
import com.example.mini_examples.p1.B;
import com.example.mini_examples.p1.C;
import com.example.mini_examples.p2.D;
import com.example.mini_examples.p2.E;

public class AccessModifierExample {

    public static void main(String[] args) {

        A a = new A();
        a.aObjectMethod();

        System.out.println("-------------------");

        B b = new B();
        b.bObjectMethod();

        System.out.println("-------------------");

        C c = new C();
        c.cObjectMethod();

        System.out.println("-------------------");

        D d = new D();
        d.dObjectMethod();

        System.out.println("-------------------");

        E e = new E();
        e.eObjectMethod();

    }
}
