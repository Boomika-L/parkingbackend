package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Slot;

import java.util.List;

public interface SlotRepo extends JpaRepository<Slot, Long> {
    List<Slot> findByUserId(Long userId);
}