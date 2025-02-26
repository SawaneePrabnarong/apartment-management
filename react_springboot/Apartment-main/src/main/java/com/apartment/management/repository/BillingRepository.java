package com.apartment.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.apartment.management.model.Billing;
import com.apartment.management.model.Room;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {
    // ค้นหาบิลตามห้องและเดือน
    List<Billing> findByRoomAndMonth(Room room, String month);
    List<Billing> findByMonth(String month);
    @Query("SELECT b FROM Billing b JOIN FETCH b.room r ORDER BY CAST(r.roomNumber AS int) ASC")
    List<Billing> findAllOrderByRoomNumber();

    
    
}