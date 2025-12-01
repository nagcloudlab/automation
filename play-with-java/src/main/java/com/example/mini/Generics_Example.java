package com.example.mini;

import java.util.ArrayList;

class Food {
}

class Egg extends Food {
}

class Pizza extends Food {
}

// Generic class ( Template class
class Box<E extends Food> {
    E item;
}


public class Generics_Example {
    public static void main(String[] args) {

        Box<Egg> eggBox = new Box<Egg>();
        eggBox.item = new Egg();

        Box<Pizza> pizzaBox = new Box<Pizza>();
        pizzaBox.item = new Pizza();

        //---------------

        //pizzaBox.item=new Egg();  // Compilation Error , void type safety

        //------------

        //Box<Account> accountBox = new Box<>();


        //-----------------------------------------------------------

        ArrayList<Egg> eggList = new ArrayList<Egg>();
        eggList.add(new Egg());
        eggList.add(new Egg());
        eggList.add(new Egg());
        eggList.add(new Egg());
        //eggList.add(new Pizza());


    }
}
