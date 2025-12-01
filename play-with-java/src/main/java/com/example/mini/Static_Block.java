package com.example.mini;


class TestCase {

    static {
        // Why use static block?
        // Static block is used to initialize static variables
        // or to perform static initialization tasks.
        // Load web driver
        System.out.println("Loading web driver");
    }

    public void test_method1() {
        System.out.println("This is a test case");
    }

    public void test_method2() {
        System.out.println("This is a test case");
    }
}

public class Static_Block {
    public static void main(String[] args) {

        TestCase tc = new TestCase();
        tc.test_method1();
        tc.test_method2();

        TestCase tc2 = new TestCase();
        tc2.test_method1();
        tc2.test_method2();

    }
}
