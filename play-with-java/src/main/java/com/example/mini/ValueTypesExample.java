package com.example.mini;

public class ValueTypesExample {
    public static void main(String[] args) {

        // Value Types

        byte b = 127; // 8-bit , range: -128 to 127
        short s = 32767; // 16-bit , range: -32,768 to 32,767
        int i = 2147483647; // 32-bit , range: -2,147,483,648 to 2,147,483,647
        long l = 9223372036854775807L; // 64-bit , range: -9,223,372,036,854,775,808 to 9,223,372,036
        float f = 3.4028235e+38F; // 32-bit IEEE 754
        double d = 1.7976931348623157e+308; // 64-bit IEEE 754
        char c1 = 'A'; // 16-bit Unicode character
        char c2 = 65; // Unicode code point for 'A'
        char c3 = '\u0041'; // Unicode escape for 'A'
        boolean boolTrue = true; // true or false
        boolean boolFalse = false; // true or false

        // ------------------------------------

        long criditCardNumber = 1234_5678_9012_3456L;

        // ------------------------------------

        // int n = 10; // Decimal representation (base 10)
        // int n = 010; // Octal representation (base 8), equals 8 in decimal
        // int n = 0x10; // Hexadecimal representation (base 16), equals 16 in decimal
        // int n = 0b10; // Binary representation (base 2), equals 2 in decimal
        // System.out.println("Value of n: " + n);

        // ------------------------------------

        int n1 = 6;
        int n2 = 4;
        float c = (float) n1 / n2;
        System.out.println("Value of c: " + c);

        // ------------------------------------

        // Type Casting

        // Implicit Casting ( JRE does this automatically )
        // Explicit Casting ( Programmer has to do this manually )

        // ------------------------------------

        int v = 456;
        byte w = (byte) v;
        System.out.println("Value of w: " + w);

    }
}
