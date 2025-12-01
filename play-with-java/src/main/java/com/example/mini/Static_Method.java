package com.example.mini;

class FoodFactory {
    // class's static method
    public static String getFood(String foodType) {
        if (foodType.equalsIgnoreCase("Pizza")) {
            return "You ordered a Pizza!";
        } else if (foodType.equalsIgnoreCase("Burger")) {
            return "You ordered a Burger!";
        } else {
            return "Sorry, we don't have that food item.";
        }
    }
}

public class Static_Method {
    public static void main(String[] args) {
//        FoodFactory factory = new FoodFactory();
//        System.out.println(factory.getFood("Pizza"));
//        System.out.println(FoodFactory.getFood("Pizza"));


        double amount=1000;
        String s=String.format("\u8377%.5f", amount);
        System.out.println(s);
    }
}
