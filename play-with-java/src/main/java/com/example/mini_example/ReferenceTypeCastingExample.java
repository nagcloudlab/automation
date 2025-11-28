package com.example.mini_example;

class V {
    void start() {
        System.out.println("Vehicle started");
    }
}

class B extends V {
    @Override
    void start() {
        System.out.println("B type Vehicle started");
    }
}

class C extends V {
    @Override
    void start() {
        System.out.println("C type Vehicle started");
    }

    void startAc() {
        System.out.println("C type Vehicle AC started");
    }
}

class Bus extends V {
    @Override
    void start() {
        System.out.println("Bus type Vehicle started");
    }
}

class Mechanic {
    // public void repair(C c) {
    // System.out.println("Repairing C type vehicle");
    // }

    // public void repair(B b) {
    // System.out.println("Servicing B type vehicle");
    // }

    // public void repair(Bus bus) {
    // System.out.println("Servicing Bus type vehicle");
    // }
    public void repair(V v) {
        System.out.println("Servicing Vehicle of type: ");
        v.start();
        if (v instanceof C) {
            C c = (C) v; // Downcasting
            c.startAc();
        }
    }
}

public class ReferenceTypeCastingExample {

    public static void main(String[] args) {

        B b = new B();
        C c = new C();
        Bus bus = new Bus();

        Mechanic mechanic = new Mechanic();
        mechanic.repair(b); // Servicing B type vehicle
        mechanic.repair(c); // Repairing C type vehicle
        mechanic.repair(bus); // Servicing Bus type vehicle

        // --------------

        int n = 456;
        byte by = (byte) n;

    }

}
