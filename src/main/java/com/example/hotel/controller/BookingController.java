package com.example.hotel.controller;

import com.example.hotel.model.Booking;
import com.example.hotel.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin("*")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @PostMapping("/save")
    public Booking saveBooking(@RequestBody Booking booking) {
        return bookingRepository.save(booking);
    }

    // Lightweight stub for order creation so the project builds and runs
    // without requiring the Razorpay SDK or API keys. Replace with
    // a real integration when you have keys and dependency added.
    @PostMapping("/createOrder")
    public Map<String, Object> createOrder(@RequestBody Map<String, Object> data) {
        int amount = 50000; // default amount in paise
        Object a = data.get("amount");
        if (a instanceof Number) {
            amount = ((Number) a).intValue();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", "order_" + UUID.randomUUID().toString());
        response.put("amount", amount);
        response.put("currency", "INR");
        response.put("note", "STUB_ORDER - replace with real Razorpay integration");

        return response;
    }

}
