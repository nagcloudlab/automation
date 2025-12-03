package com.npci.mini;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*

    sorting, 2 step process
    1. compare 2 elements
    2. swap the elements based on comparison

    sorting algorithms
    1. bubble sort
    2. selection sort
    3. insertion sort
    4. merge sort
    5. quick sort
    6. heap sort
    7. radix sort
    8. bucket sort
    9. shell sort
    10. counting sort

    //--------------------------------

    Collections.sort(list); // uses TimSort (derived from merge sort and insertion sort)

 */

public class How_To_Sort_List {
    public static void main(String[] args) {


        List<Account> accounts=new ArrayList<>();
        accounts.add(new Account("ACC002", "Alice", 5000));
        accounts.add(new Account("ACC001", "Bob", 3000));
        accounts.add(new Account("ACC003", "Charlie", 1000));

        displayAccounts("Before Sorting:", accounts);
        //Collections.sort(accounts);
        displayAccounts("After Sorting by account number (natural order):", accounts);

        // sort by account by balance


        Collections.sort(accounts,(a1,a2)->Double.compare(a1.getBalance(),a2.getBalance()));
        displayAccounts("After Sorting by balance (using Comparator):", accounts);

        // sort by balance - desc
        Collections.sort(accounts, (a1,a2)->Double.compare(a2.getBalance(),a1.getBalance()));
        displayAccounts("After Sorting by balance desc (using Comparator):", accounts);
    }

    private static void displayAccounts(String s, List<Account> accounts) {
        System.out.println(s);
        for (Account account : accounts) {
            System.out.println(account);
        }
    }


}

