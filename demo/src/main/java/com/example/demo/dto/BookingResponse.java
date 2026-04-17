package com.example.demo.dto;

import lombok.Data;

@Data
public class BookingResponse {

    private Long bookingId;
    private String location;
    private double price;
}