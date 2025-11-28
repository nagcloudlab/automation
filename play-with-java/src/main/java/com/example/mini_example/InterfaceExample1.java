package com.example.mini_example;

// design issues

//-----------------
// -> tight coupling b/w dependent and dependency
//-----------------

interface Wheel {
    int rotate(int speed);
}

// MRF
class MRFWheel implements Wheel {
    public int rotate(int speed) {
        System.out.println("MRF Wheel is rotating");
        return 2 * speed;
    }
}

// JK
class JKWheel implements Wheel {
    public int rotate(int speed) {
        System.out.println("JK Wheel is rotating");
        return 3 * speed;
    }
}

class Car {
    // HAS-A relationship
    // MRFWheel wheel = new MRFWheel();
    // JKWheel wheel = new JKWheel();
    private Wheel wheel;

    // setter
    public void setWheel(Wheel wheel) {
        this.wheel = wheel;
    }

    public void move() {
        int rpm = wheel.rotate(100);
        System.out.println("Car is moving at speed: " + rpm);
    }
}

public class InterfaceExample1 {

    public static void main(String[] args) {

        Car myCar = new Car();
        MRFWheel mrfWheel = new MRFWheel();
        JKWheel jkWheel = new JKWheel();
        myCar.setWheel(mrfWheel);
        myCar.move();

        // after 25km
        myCar.setWheel(jkWheel);

        myCar.move();

    }

}
