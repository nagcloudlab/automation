package com.example.mini_example;

import java.lang.*;


class Emp {
    int id;
    String name;

    Emp(int id, String name) {
        this.id = id;
        this.name = name;
    }

    void doWork() {
        System.out.println("Employee ID: " + id + ", Name: " + name);
    }
}

public class Unchecked_Exception_Example {

    public static void main(String[] args) {

        int[] numbers = {1, 2, 3};

        try {
            int index = 5; // This index is out of bounds
            int n = numbers[index];
            System.out.println("Number at index " + index + ": " + n);
            int result = 10 / 10;
            System.out.println("Result of division: " + result);
            Emp emp = null;
            emp.doWork();
            System.out.println("All is well!");
        } catch (ArrayIndexOutOfBoundsException | ArithmeticException e) {
            System.out.println("Array index is out of bounds: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Null reference encountered: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Runtime exception occurred: " + e.getMessage());
        }


    }

}
