package com.example.mini_example;
// Ref : https://docs.oracle.com/javase/tutorial/essential/exceptions/advantages.html


//------------------------------------------
// team-1 : TransferService
//------------------------------------------

class InsufficientFundsException extends Throwable {
    String message;

    public InsufficientFundsException(String message) {
        this.message = message;
    }
}

class TransferService {
    public void transferFunds(String fromAccount, String toAccount, double amount) throws InsufficientFundsException {
        // Load account details (simulated) ...
        double fromAccountBalance = 100.0; // Simulated balance
        if (amount > fromAccountBalance) {
            InsufficientFundsException ife = new InsufficientFundsException("Insufficient funds in account: " + fromAccount);
            throw ife;
        }
    }
}


//------------------------------------------
// team-2 : TicketBookingService
//------------------------------------------

class TicketBookingService {
    TransferService transferService = new TransferService();

    public void bookTicket(String user, String movie) {
        // Simulate ticket booking logic
        if (movie == null || movie.isEmpty()) {
        }
        // Simulate payment processing
        try {
            transferService.transferFunds("userAccount", "theaterAccount", 1000.0);
            System.out.println("Ticket booked for " + user + " to watch " + movie);
        } catch (InsufficientFundsException ife) {
            //.....
            // Plan B : Handle the exception gracefully
            // Log & re-throw
            // release resources ( e.g DB connections, file handles etc.,)
            // friendly message to user
            System.out.println("Failed to book ticket: " + ife.message);
        }
    }
}


public class How_To_Handle_Exception {
    public static void main(String[] args) {


        TicketBookingService bookingService = new TicketBookingService();
        bookingService.bookTicket("Alice", "Inception");

    }
}
