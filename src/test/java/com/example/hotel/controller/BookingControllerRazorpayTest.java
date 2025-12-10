package com.example.hotel.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookingControllerRazorpayTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    static void setup() {
        // Set Razorpay credentials for testing
        System.setProperty("razorpay.key", "rzp_test_RpnzkeU0auAn5n");
        System.setProperty("razorpay.secret", "DoLzsqBtOwNNlfUu4BlZ6MOp");
    }

    @Test
    void testCreateOrderWithRazorpay() {
        Map<String, Object> requestBody = Map.of("amount", 3000);

        @SuppressWarnings("unchecked")
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "/api/bookings/createOrder",
                requestBody,
                Map.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertNotNull(responseBody.get("id"));
        assertEquals(3000, responseBody.get("amount"));
        assertEquals("INR", responseBody.get("currency"));

        // Verify it's a real Razorpay order (should start with "order_" and not be a UUID stub)
        String orderId = responseBody.get("id").toString();
        System.out.println("DEBUG: Got order ID: " + orderId);
        System.out.println("DEBUG: Full response: " + responseBody);
        
        assertTrue(orderId.startsWith("order_"), "Order ID should start with 'order_': " + orderId);
        // Real Razorpay orders don't have the UUID-style dashes in the ID part after order_
        // Example real Razorpay: order_RpoRJG6RCmMbzX (alphanumeric, no dashes)
        // UUID stub: order_380e6f6f-51ed-4934-b1af-6cc571d23cbe (has dashes from UUID)
        String idPart = orderId.substring(6); // Remove "order_" prefix
        assertFalse(idPart.contains("-"), "Real Razorpay order ID should not contain dashes after 'order_' prefix. Got: " + orderId);
    }

}
