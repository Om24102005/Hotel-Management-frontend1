package com.example.hotel.controller;

import com.example.hotel.model.Booking;
import com.example.hotel.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin("*")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<Booking> listAll() {
        return bookingService.listAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getById(@PathVariable Long id) {
        try {
            Booking b = bookingService.findById(id);
            return ResponseEntity.ok(b);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/save")
    public ResponseEntity<Booking> saveBooking(@Valid @RequestBody Booking booking) {
        Booking saved = bookingService.create(booking);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookingService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Keep stubbed createOrder for now (returns fake order); can be replaced by real Razorpay integration.
    @PostMapping("/createOrder")
    public Map<String, Object> createOrder(@RequestBody Map<String, Object> data) {
        // Try to get Razorpay keys from properties or environment variables
        String key = System.getenv().getOrDefault("RAZORPAY_KEY", System.getProperty("razorpay.key", ""));
        String secret = System.getenv().getOrDefault("RAZORPAY_SECRET", System.getProperty("razorpay.secret", ""));

        int amount = 50000;
        Object a = data.get("amount");
        if (a instanceof Number) {
            amount = ((Number) a).intValue();
        }

        if (!key.isBlank() && !secret.isBlank()) {
            try {
                com.razorpay.RazorpayClient client = new com.razorpay.RazorpayClient(key, secret);
                org.json.JSONObject orderRequest = new org.json.JSONObject();
                orderRequest.put("amount", amount);
                orderRequest.put("currency", "INR");
                orderRequest.put("receipt", "booking_rcpt_" + UUID.randomUUID().toString());
                com.razorpay.Order order = client.Orders.create(orderRequest);
                return Map.of(
                        "id", order.get("id"),
                        "amount", order.get("amount"),
                        "currency", order.get("currency")
                );
            } catch (Exception ex) {
                // fall through to stubbed response on error
            }
        }

        return Map.of(
                "id", "order_" + UUID.randomUUID().toString(),
                "amount", amount,
                "currency", "INR",
                "note", "STUB_ORDER"
        );
    }

}
