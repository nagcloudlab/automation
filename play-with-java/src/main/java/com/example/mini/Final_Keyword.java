package com.example.mini;

class BankAccount {
    static final String bankName="NPCI bank"; // static final variable
    int accountNumber; // instance variable
}


class Super{
    final void display(){
        System.out.println("This is a final method.");
    }
}

class Sub extends Super {
//   void display(){
//        System.out.println("This is an overridden method.");
//    }
}


class LT{}
class Human extends LT{}
class Man extends Human{}
final class Boy extends Man{}
//class Foo extends Boy{}
//class MyString extends String{}

public class Final_Keyword {
    public static void main(String[] args) {

        int v = 10;
        v = 20; // valid

        final int a = 30; /// local variable
//         a=40; // invalid, compile-time error

        BankAccount account1 = new BankAccount();
        BankAccount account2 = new BankAccount();

        //--------------------------------------

        // to read static final variable
        System.out.println("Bank Name: " + BankAccount.bankName);

        // to read instance variable
        account1.accountNumber = 1001;
        account2.accountNumber = 1002;
        System.out.println("Account 1 Number: " + account1.accountNumber);
        System.out.println("Account 2 Number: " + account2.accountNumber);

    }
}
