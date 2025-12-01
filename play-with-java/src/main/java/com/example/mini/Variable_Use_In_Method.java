package com.example.mini;


class SomeClass {
    static int staticVar = 1;
    int instanceVar = 2;

    static void staMethod() {
        System.out.println(staticVar);
//        System.out.println(instanceVar);//---> Error
    }

    void insMethod() {
        System.out.println(staticVar);
        System.out.println(instanceVar);
    }
}

public class Variable_Use_In_Method {
    public static void main(String[] args) {

        SomeClass.staMethod();

        SomeClass obj = new SomeClass();
        obj.insMethod();

    }
}
