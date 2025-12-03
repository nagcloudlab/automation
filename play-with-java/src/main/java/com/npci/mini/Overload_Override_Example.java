package com.npci.mini;

// coding rules for method overloading
//--------------------------------------
// 1. same method name
// 2. different parameter list
//    a. different number of parameters
//    b. different type of parameters
//    c. different sequence of parameters
// 3. same or different return type
// 4. same or different access modifier

// coding rules for method overriding
//---------------------------------------
// 1. same method name
// 2. same parameter list
// 3. same return type
// 4. same or more visible access modifier  

// author : Nag
abstract class Acc {
    // v1
    public void deposit(double aount, int pin) {
        System.out.println("Account Deposit");
    }

    // v2
    public void deposit(int pin, double amount) {
        System.out.println("Account Deposit");
    }

    public abstract void withdraw(double amount, int pin);
}

// author: Boy team
class SavingsAccount extends Acc {
    // v1
    @Override
    public void withdraw(double amount, int pin) {
        System.out.println("SavingsAccount Withdraw");
    }

    // v2
    public void withdraw(double amount) {
        System.out.println("SavingsAccount Withdraw - overloaded");
    }
}

// team: Girl team
class CurrentAccount extends Acc {
    @Override
    public void withdraw(double amount, int pin) {
        System.out.println("CurrentAccount Withdraw");
    }
}

class PaymentGateway {
    public void witgdrawFunds(Acc account, double amount, int pin) {
        account.withdraw(amount, pin);
    }
}

public class Overload_Override_Example {

    public static void main(String[] args) {

        SavingsAccount sa = new SavingsAccount();
        CurrentAccount ca = new CurrentAccount();

        PaymentGateway pg = new PaymentGateway();
        pg.witgdrawFunds(sa, 300.0, 1234); // dynamic method dispatch
        pg.witgdrawFunds(ca, 400.0, 5678); // dynamic method dispatch

    }

}
