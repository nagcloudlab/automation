package com.example.mini_example;

import java.io.*;


public class Checked_Exception {
    public static void main(String[] args) {
        FileReader fileReader = null;
        try {
            // init...
            fileReader = new FileReader("accounts.csv"); // Opening a file
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            // use
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (FileNotFoundException fnfe) {
            //...handle exception
            System.out.println("File not found: " + fnfe.getMessage());
        } catch (IOException ioe) {
            //...handle exception
            System.out.println("I/O error: " + ioe.getMessage());
        } finally {
            // Why we need?
            // To close resources like file streams, network connections, etc.
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (IOException ioe) {
                System.out.println("Error closing file: " + ioe.getMessage());
            }
        }
    }
}


/*
// SUmmary:
    to throw exception : use ' throw ' keyword
    to force invoker method to handle exception : use 'throws ' keyword
    to handle exception : use ' try-catch-finally' block
 */

