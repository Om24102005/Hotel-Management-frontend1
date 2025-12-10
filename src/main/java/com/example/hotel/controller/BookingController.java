package com.example.hotel.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.hotel.model.Booking;
import com.example.hotel.service.BookingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin("*")
public class BookingController {

    private final BookingService bookingService;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String RAZORPAY_API_URL = "https://api.razorpay.com/v1/orders";

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

    @PostMapping("/createOrder")
    public Map<String, Object> createOrder(@RequestBody Map<String, Object> data) {
        String key = getOrDefault(System.getProperty("razorpay.key"), System.getenv("RAZORPAY_KEY"), "");
        String secret = getOrDefault(System.getProperty("razorpay.secret"), System.getenv("RAZORPAY_SECRET"), "");

        System.out.println("DEBUG: razorpay.key='" + key + "', razorpay.secret='" + (secret != null && !secret.isEmpty() ? "[redacted]" : "") + "'");

        int amount = 50000;
        if (data.get("amount") instanceof Number) {
            amount = ((Number) data.get("amount")).intValue();
        }

        if (!key.isBlank() && !secret.isBlank()) {
            try {
                // Receipt must be max 40 chars - use short format
                long receiptId = System.currentTimeMillis() % 1000000;
                String receipt = "receipt_" + receiptId;
                
                Map<String, Object> payload = Map.of(
                        "amount", amount,
                        "currency", "INR",
                        "receipt", receipt
                );

                HttpHeaders headers = new HttpHeaders();
                headers.setBasicAuth(key, secret);
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

                System.out.println("DEBUG: Calling Razorpay API at " + RAZORPAY_API_URL);
                
                @SuppressWarnings({"unchecked", "rawtypes"})
                ResponseEntity<Map> response = restTemplate.postForEntity(
                        RAZORPAY_API_URL,
                        request,
                        Map.class
                );

                if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> resp = (Map<String, Object>) response.getBody();
                    if (resp != null && resp.containsKey("id")) {
                        System.out.println("SUCCESS: Razorpay order created: " + resp.get("id"));
                        return Map.of(
                                "id", resp.get("id"),
                                "amount", resp.get("amount"),
                                "currency", resp.get("currency")
                        );
                    }
                }
            } catch (HttpClientErrorException | HttpServerErrorException ex) {
                System.out.println("ERROR HTTP " + ex.getStatusCode() + ": " + ex.getResponseBodyAsString());
            } catch (Exception ex) {
                System.out.println("ERROR calling Razorpay: " + ex.getClass().getName() + " - " + ex.getMessage());
            }
        }

        return Map.of(
                "id", "order_" + UUID.randomUUID().toString(),
                "amount", amount,
                "currency", "INR",
                "note", "STUB_ORDER"
        );
    }

    private String getOrDefault(String... values) {
        for (String val : values) {
            if (val != null && !val.isBlank()) {
                return val;
            }
        }
        return "";
    }

}