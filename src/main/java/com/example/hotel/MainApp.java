package com.example.hotel;

import com.example.hotel.model.Booking;

public class MainApp {
    public static void main(String[] args) {
        Booking b = new Booking("Alice", "alice@example.com", "555-0100",
                "Deluxe", "2025-12-10", "2025-12-12", 2);
        System.out.println("Booking: name=" + b.getName() + ", email=" + b.getEmail() + ", room=" + b.getRoomType());
    }
}
