package com.example.demo.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.dto.BookingResponse;
import java.util.*;
@RestController
@CrossOrigin
public class MainController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private SlotRepo slotRepo;
    @Autowired
    private BookingRepo bookingRepo;
    @PostMapping("/register")
    public Users register(@RequestBody Users user) {
        return userRepo.save(user);
    }
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Users user) {
        Users u = userRepo.findByEmail(user.getEmail());
        if (u != null && u.getPassword().equals(user.getPassword())) {
            return Map.of("token", u.getId().toString());
        }
        return Map.of("token", "");
    }

    @GetMapping("/slots")
    public List<Slot> getSlots() {
        return slotRepo.findAll();
    }
    @PostMapping("/book/{id}")
    public Booking bookSlot(@PathVariable Long id,
                            @RequestHeader("Authorization") String token) {
        Long userId = Long.parseLong(token.replace("Bearer ", ""));
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setSlotId(id);
        return bookingRepo.save(booking);
    }
    @GetMapping("/my-bookings")
public List<BookingResponse> getMyBookings(
        @RequestHeader("Authorization") String token) {

    Long userId = Long.parseLong(token.replace("Bearer ", ""));

    List<Booking> bookings = bookingRepo.findByUserId(userId);
    List<BookingResponse> response = new ArrayList<>();

    for (Booking b : bookings) {

        Slot slot = slotRepo.findById(b.getSlotId()).orElse(null);

        if (slot != null) {
            BookingResponse br = new BookingResponse();
            br.setBookingId(b.getId());
            br.setLocation(slot.getLocation());
            br.setPrice(slot.getPrice());

            response.add(br);
        }
    }

    return response;
}
    @PostMapping("/slots")
    public Slot addSlot(@RequestBody Slot slot,
                        @RequestHeader("Authorization") String token) {

        Long userId = Long.parseLong(token.replace("Bearer ", ""));
        slot.setUserId(userId);
        return slotRepo.save(slot);
    }
    @GetMapping("/my-slots")
    public List<Slot> getMySlots(@RequestHeader("Authorization") String token) {

        Long userId = Long.parseLong(token.replace("Bearer ", ""));
        return slotRepo.findByUserId(userId);
    }
    @DeleteMapping("/cancel/{id}")
public void cancelBooking(@PathVariable Long id) {
    bookingRepo.deleteById(id);
}
}