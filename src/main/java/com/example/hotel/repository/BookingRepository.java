package com.example.hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.hotel.model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

}
