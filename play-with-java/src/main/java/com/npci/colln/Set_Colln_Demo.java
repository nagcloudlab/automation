package com.npci.colln;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

public class Set_Colln_Demo {
    public static void main(String[] args) {


        Account charlieAccount = new Account("ACC003", "Charlie", 7000);
        Account aliceAccount = new Account("ACC001", "Alice", 5000);
        Account bobAccount = new Account("ACC002", "Bob", 3000);
        Account charlieAccountDup = new Account("ACC003", "Charlie", 7000);


//        // print hashcode of accounts
//        System.out.println("HashCodes:");
//        System.out.println(charlieAccount.hashCode());
//        System.out.println(aliceAccount.hashCode());
//        System.out.println(bobAccount.hashCode());
//        System.out.println(charlieAccountDup.hashCode());
//        // print equals of accounts
//        System.out.println("Equals:");
//        System.out.println(charlieAccount.equals(aliceAccount));
//        System.out.println(charlieAccount.equals(bobAccount));
//        System.out.println(charlieAccount.equals(charlieAccountDup));

//        Set<Account> accounts=new TreeSet<>((a1,a2)->{
//            return a1.getAccountNumber().compareTo(a2.getAccountNumber());
//        });
        //Set<Account> accounts = new HashSet<>();
        Set<Account> accounts = new LinkedHashSet<>();

        accounts.add(aliceAccount);
        accounts.add(charlieAccount);
        accounts.add(bobAccount);
        accounts.add(charlieAccountDup); // duplicate based on account number

        displayAccounts(accounts);


    }

    private static void displayAccounts(Set<Account> accounts) {
        for (Account acc : accounts) {
            System.out.println(acc);
        }
    }
}
