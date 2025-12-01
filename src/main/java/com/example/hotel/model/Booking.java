package com.example.hotel.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;
    private String roomType;
    private String checkin;
    private String checkout;
    private int guests;

    // Required by JPA
    public Booking() {}

    public Booking(String name, String email, String phone,
                   String roomType, String checkin, String checkout, int guests) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.roomType = roomType;
        this.checkin = checkin;
        this.checkout = checkout;
        this.guests = guests;
    }

    public Booking(String checkin, String checkout, String email, int guests, Long id, String name, String phone, String roomType) {
        this.checkin = checkin;
        this.checkout = checkout;
        this.email = email;
        this.guests = guests;
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.roomType = roomType;
    }

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }

    public String getCheckin() { return checkin; }
    public void setCheckin(String checkin) { this.checkin = checkin; }

    public String getCheckout() { return checkout; }
    public void setCheckout(String checkout) { this.checkout = checkout; }

    public int getGuests() { return guests; }
    public void setGuests(int guests) { this.guests = guests; }

    public void setId(Long id) {
        this.id = id;
    }
}
