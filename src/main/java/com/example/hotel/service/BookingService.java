package com.example.hotel.service;

import com.example.hotel.model.Booking;
import com.example.hotel.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking create(Booking booking) {
        return bookingRepository.save(booking);
    }

    public List<Booking> listAll() {
        return bookingRepository.findAll();
    }

    public Booking findById(Long id) {
        return bookingRepository.findById(id).orElseThrow(() -> new RuntimeException("Booking not found: " + id));
    }

    public void delete(Long id) {
        bookingRepository.deleteById(id);
    }
}
