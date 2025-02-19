package com.apartment.management.repository;

import com.apartment.management.model.FloorPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FloorPriceRepository extends JpaRepository<FloorPrice, Integer> {
}
