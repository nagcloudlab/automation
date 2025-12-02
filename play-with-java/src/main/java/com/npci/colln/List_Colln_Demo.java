package com.npci.colln;

import java.util.*;

public class List_Colln_Demo {
    public static void main(String[] args) {

        ArrayList<Account> accounts = new ArrayList<>(); // box
        //---------------------------
        //|   |  |  |  |  |  |   |
        //---------------------------
        // List can hold duplicate values
        accounts.add(new Account("ACC001", "Alice", 5000));
        accounts.add(new Account("ACC002", "Bob", 3000));
        accounts.add(new Account("ACC003", "Charlie", 7000));
        accounts.add(new Account("ACC002", "Bob", 3000));

        //----------------------------
        // index based access
        //Account account = accounts.get(2);
        //System.out.println(account);
        //----------------------------

        // iterate // looping
//        for(Account acc:accounts){
//            System.out.println(acc);
//        }

        //----------------------------

//        List
        // Vector
        // ArrayList
        // LinkedList

        //----------------------------

//        compareList(new Vector<>());
//        compareList(new ArrayList<>());
//        compareList(new LinkedList<>());

        //--------------------------------

        // List's Methods

        //------------------------------

        // add()
        List<String> names = new ArrayList<>();
        names.add("Alice");
        names.add("Bob");
        names.add("Charlie");
        System.out.println("Names: " + names);

        // get()
        String nameAtIndex1 = names.get(1);
        System.out.println("Name at index 1: " + nameAtIndex1);

        // size()
        int size = names.size();
        System.out.println("Size of names list: " + size);

        // remove()
        names.remove("Bob");
        System.out.println("Names after removal: " + names);

        // remove at index
        names.remove(0);
        System.out.println("Names after removing index 0: " + names);

        // contains()
        boolean hasCharlie = names.contains("Charlie");
        System.out.println("List contains Charlie: " + hasCharlie);

        // containsAll()
        List<String> moreNames = Arrays.asList("Charlie", "David");
        boolean containsAll = names.containsAll(moreNames);
        System.out.println("Names contains all moreNames: " + containsAll);

        // isEmpty()
        boolean isEmpty = names.isEmpty();
        System.out.println("Is names list empty: " + isEmpty);

        // clear()
        names.clear();
        System.out.println("Names after clear: " + names);

        // addAll()
        List<String> newNames = Arrays.asList("Eve", "Frank");
        names.addAll(newNames);
        System.out.println("Names after addAll: " + names);

        // set()
        names.set(0, "Evelyn");

        // indexOf()
        int index = names.indexOf("Frank");
        System.out.println("Index of Frank: " + index);

        // lastIndexOf()
        names.add("Evelyn");
        int lastIndex = names.lastIndexOf("Evelyn");
        System.out.println("Last index of Evelyn: " + lastIndex);

        // toArray()
        Object[] namesArray = names.toArray();
        System.out.println("Names array: " + Arrays.toString(namesArray));

        // subList()
        List<String> subList = names.subList(0, 1);
        System.out.println("Sublist: " + subList);


        // iterator()
        Iterator<String> iterator = names.iterator();
        System.out.print("Names using iterator: ");
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
        System.out.println();
        // or
        for (String name : names) {
            System.out.print(name + " ");
        }

        //------------------------------------

        List<String> sheet1 = new ArrayList<>();
        sheet1.add("A");
        sheet1.add("B");
        sheet1.add("C");

        List<String> sheet2 = new ArrayList<>();
        sheet2.add("B");
        sheet2.add("C");
        sheet2.add("D");

        //sheet1.removeAll(sheet2);
        sheet1.retainAll(sheet2);
        System.out.println("sheet1 after removeAll: " + sheet1);


    }


    public static void compareList(List<Account> list) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
            Account account = new Account("ACC" + i, "User" + i, i * 1000);
            list.add(account);
        }
        long end = System.currentTimeMillis();
        System.out.println("Time taken: " + (end - start) + " ms");
    }
}
