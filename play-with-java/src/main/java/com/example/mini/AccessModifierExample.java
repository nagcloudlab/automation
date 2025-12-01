package com.example.mini;

import com.example.mini.p1.A;
import com.example.mini.p1.B;
import com.example.mini.p1.C;
import com.example.mini.p2.D;
import com.example.mini.p2.E;

// proj
//     |
//     |-- p1
//     |   - A.java
//     |   - B.java extends A
//     |   - C.java
//     |
//     |-- p2
//         - D.java extends C
//         - E.java

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
