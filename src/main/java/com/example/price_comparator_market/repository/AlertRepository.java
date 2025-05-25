package com.example.price_comparator_market.repository;

import com.example.price_comparator_market.model.Alert;
import com.example.price_comparator_market.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByStatus(Status status);
}