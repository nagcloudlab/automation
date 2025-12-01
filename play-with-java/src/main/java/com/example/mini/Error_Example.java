package com.example.mini;

class E {
    int v1; // 32 bits
    double v2; // 64 bits
}

public class Error_Example {
    public static void main(String[] args) {

        try {
            int current_memory = 1024;
            if (current_memory < 2048) {
                throw new OutOfMemoryError("Memory is insufficient");
            } else {
                System.out.println("Memory is sufficient");
            }
        }catch (OutOfMemoryError e){
            //..
        }
    }
}
