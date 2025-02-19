package com.apartment.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apartment.management.model.Billing;
import com.apartment.management.model.Room;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {
    // ค้นหาบิลตามห้องและเดือน
    List<Billing> findByRoomAndMonth(Room room, String month);
    List<Billing> findByMonth(String month);
    
}
