package com.apartment.management.repository;

import com.apartment.management.model.ElectricMeter;
import com.apartment.management.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

//import java.time.YearMonth;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ElectricMeterRepository extends JpaRepository<ElectricMeter, Long> {
    // ค้นหามิเตอร์ไฟตามห้องและเดือน
    List<ElectricMeter> findByRoomAndRecordDateBetween(Room room, LocalDate startDate, LocalDate endDate);

    List<ElectricMeter> findByRecordDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT em FROM ElectricMeter em WHERE em.room = :room AND YEAR(em.recordDate) = :year AND MONTH(em.recordDate) = :month")
    Optional<ElectricMeter> findByRoomAndRecordMonth(@Param("room") Room room, @Param("year") int year,
            @Param("month") int month);

}
