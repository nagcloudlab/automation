package com.npci.mini;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


    /*
        style of programming
        ---------------------
        => imperative programming
            => how to do
            => step by step instructions
            => focus on the process
            => mutable data
            problem:
             => intention & implementation mixed up

         => declarative  programming
            => what to do
            => focus on the intention
            => immutable data
            solution:
             => intention & implementation separated
             declarative programming can be achieved using function/lambdas => functional programming
     */


public class Why_FP {
    public static void main(String[] args) {

        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10); // immutable List

        // Req-1
        //List<Integer> oddNumbers = filterOddNumbers(numbers);
        Predicate<Integer> oddPredicate = input -> input % 2 != 0;
        System.out.println(oddPredicate);
        List<Integer> oddNumbers = filterNumbers(numbers, oddPredicate);


        // Req-2
        //List<Integer> evenNumbers = filterEvenNumbers(numbers);
        List<Integer> evenNumbers = filterNumbers(numbers, input -> input % 2 == 0);
        System.out.println(evenNumbers);

        // Req-3
        //List<Integer> numbersGT5 = filterNumbersGTFive(numbers);
        List<Integer> numbersGT5 = filterNumbers(numbers, input -> input > 5);
        System.out.println(numbersGT5);

        //-----------

    }

    private static List<Integer> filterNumbers(List<Integer> inputList, Predicate<Integer> obj) {
        List<Integer> outputList = new java.util.ArrayList<>();
        for (int n : inputList) {
            if (obj.test(n)) {
                outputList.add(n);
            }
        }
        return outputList;
    }

    private static List<Integer> filterOddNumbers(List<Integer> inputList) {
        List<Integer> outputList = new java.util.ArrayList<>();
        for (int n : inputList) {
            if (n % 2 != 0) {
                outputList.add(n);
            }
        }
        return outputList;
    }

    private static List<Integer> filterEvenNumbers(List<Integer> inputList) {
        List<Integer> outputList = new java.util.ArrayList<>();
        for (int n : inputList) {
            if (n % 2 == 0) {
                outputList.add(n);
            }
        }
        return outputList;
    }

    private static List<Integer> filterNumbersGTFive(List<Integer> inputList) {
        List<Integer> outputList = new java.util.ArrayList<>();
        for (int n : inputList) {
            if (n > 5) {
                outputList.add(n);
            }
        }
        return outputList;
    }
}

