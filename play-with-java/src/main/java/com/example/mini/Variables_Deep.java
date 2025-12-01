package com.example.mini;

class Employee {
    final static String companyName = "TechCorp"; // Class variable
    static String projectName; // null
    final int id; // Instance variable

    Employee(int id) {
        this.id = id;
    }
}

public class Variables_Deep {
    public static void main(String[] args) {


        Employee.projectName = "AI Development"; // Modifying class variable

        Employee e = new Employee(101); // Object creation
        System.out.println("Employee ID: " + e.id);
        //e.id=202; // Modifying instance variable
        System.out.println("Updated Employee ID: " + e.id);


        int v = 10; // Local variable declaration
        System.out.println(v);
        v = 20;

        final int x;
        x = 30;
        System.out.println(x);
        // x=40; // This would cause a compile-time error because x is final


    }
}
