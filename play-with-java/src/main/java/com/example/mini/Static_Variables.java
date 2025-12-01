package com.example.mini;

class Person {

    private static int count = 0; // Static variable

    private String name;
    //private int count = 0; // Instance variable

    public Person(String name) {
        this.name = name;
    }

    public void sayHello() {
        //int count = 0; // Local variable
        count++;
        System.out.println("Hello, my name is " + name + ". This is greeting number " + count + ".");
    }
}

public class Static_Variables {

    public static void main(String[] args) {

        Person person1 = new Person("Alice");
        Person person2 = new Person("Bob");

        person1.sayHello();
        person1.sayHello();
        person1.sayHello();

        person2.sayHello();
        person2.sayHello();

    }

}
