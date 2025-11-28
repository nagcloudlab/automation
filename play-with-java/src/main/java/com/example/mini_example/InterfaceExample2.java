package com.example.mini_example;

/*

    getTotalPrice(cartItems): double

    component(s)
        - PriceMatrix
            - getPrice(itemId): double
        - BillingService
            - getTotalPrice(cartItems): double

*/

//-------------------------------------------------
// Interface PriceMatrix
//-------------------------------------------------
// author : senior-dev
interface PriceMatrix {
    double getPrice(String itemId);
}

// ----------------------------------------
// Excelsheet PriceMatrix
// ----------------------------------------
// author : dev-1.1

class ExcelsheetPriceMatrix implements PriceMatrix {
    public double getPrice(String itemId) {
        // Simulate fetching price from an Excel sheet
        if (itemId.equals("item1")) {
            return 10.0;
        } else if (itemId.equals("item2")) {
            return 20.0;
        }
        return 0.0;
    }
}

// ------------------------------------------------
// Database PriceMatrix
// ------------------------------------------------
// author : dev-1.2

class DatabasePriceMatrix implements PriceMatrix {
    public double getPrice(String itemId) {
        // Simulate fetching price from a database
        if (itemId.equals("item1")) {
            return 12.0;
        } else if (itemId.equals("item2")) {
            return 22.0;
        }
        return 0.0;
    }
}

// ------------------------------------------------
// BillingService
// ------------------------------------------------
// author : dev-2

interface BillingService {
    double getTotalPrice(String[] cartItems);

    void setPriceMatrix(PriceMatrix priceMatrix);
}

class OnlineBillingService implements BillingService {
    private PriceMatrix priceMatrix;

    public void setPriceMatrix(PriceMatrix priceMatrix) {
        this.priceMatrix = priceMatrix;
    }

    public double getTotalPrice(String[] cartItems) {
        double total = 0.0;
        for (String itemId : cartItems) {
            total += priceMatrix.getPrice(itemId);
        }
        return total;
    }
}

class OfflineBillingService implements BillingService {
    private PriceMatrix priceMatrix;

    public void setPriceMatrix(PriceMatrix priceMatrix) {
        this.priceMatrix = priceMatrix;
    }

    public double getTotalPrice(String[] cartItems) {
        double total = 0.0;
        for (String itemId : cartItems) {
            total += priceMatrix.getPrice(itemId);
        }
        return total - 100; // Offline discount of 100
    }
}

// ----------------------------------------

public class InterfaceExample2 {

    public static void main(String[] args) {

        PriceMatrix excelPriceMatrix = new ExcelsheetPriceMatrix();
        PriceMatrix dbPriceMatrix = new DatabasePriceMatrix();

        BillingService onlineBilling = new OnlineBillingService();
        BillingService offlineBilling = new OfflineBillingService();
        onlineBilling.setPriceMatrix(excelPriceMatrix);
        offlineBilling.setPriceMatrix(dbPriceMatrix);

        String[] cartItems = { "item1", "item2" };
        double onlineTotal = onlineBilling.getTotalPrice(cartItems);
        double offlineTotal = offlineBilling.getTotalPrice(cartItems);

        System.out.println("Online Billing Total: " + onlineTotal);
        System.out.println("Offline Billing Total: " + offlineTotal);

    }

}
