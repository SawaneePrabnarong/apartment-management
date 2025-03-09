package com.apartment.management.service.impl;

import com.apartment.management.model.ElectricMeter;
import com.apartment.management.model.Room;
import com.apartment.management.model.WaterMeter;
import com.apartment.management.repository.ElectricMeterRepository;
import com.apartment.management.repository.RoomRepository;
import com.apartment.management.repository.WaterMeterRepository;
import com.apartment.management.service.MeterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class MeterServiceImpl implements MeterService {

    @Autowired
    private WaterMeterRepository waterMeterRepository;

    @Autowired
    private ElectricMeterRepository electricMeterRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public void saveWaterMeter(Long roomId, Double meterValue, LocalDate recordDate) throws Exception {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new Exception("Room not found with id: " + roomId));

        // ✅ ดึง `year` และ `month` จากวันที่ที่ส่งมา
        int year = recordDate.getYear();
        int month = recordDate.getMonthValue();

        // ✅ ตรวจสอบว่ามีค่ามิเตอร์ของเดือนนั้นอยู่หรือไม่
        Optional<WaterMeter> existingMeter = waterMeterRepository.findByRoomAndRecordMonth(room, year, month);

        if (existingMeter.isPresent()) {
            // ✅ อัปเดตค่ามิเตอร์ที่มีอยู่
            WaterMeter meter = existingMeter.get();
            meter.setMeterValue(meterValue);
            meter.setRecordDate(recordDate); // อัปเดตวันที่ให้แน่ใจว่าตรงกัน
            waterMeterRepository.save(meter);
            System.out.println("✅ อัปเดตค่ามิเตอร์น้ำเรียบร้อย: " + meterValue);
        } else {
            // ✅ ถ้ายังไม่มีข้อมูล → บันทึกใหม่
            WaterMeter newMeter = new WaterMeter(room, meterValue, recordDate);
            waterMeterRepository.save(newMeter);
            System.out.println("✅ บันทึกมิเตอร์น้ำใหม่เรียบร้อย: " + meterValue);
        }
    }


    @Override
    public void saveElectricMeter(Long roomId, Double meterValue, LocalDate recordDate) throws Exception {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new Exception("Room not found with id: " + roomId));
    
        // ✅ ดึง `year` และ `month` จากวันที่ที่ส่งมา
        int year = recordDate.getYear();
        int month = recordDate.getMonthValue();
    
        // ✅ ตรวจสอบว่ามีค่ามิเตอร์ของเดือนนั้นอยู่หรือไม่
        Optional<ElectricMeter> existingMeter = electricMeterRepository.findByRoomAndRecordMonth(room, year, month);
    
        if (existingMeter.isPresent()) {
            // ✅ อัปเดตค่ามิเตอร์ที่มีอยู่
            ElectricMeter meter = existingMeter.get();
            meter.setMeterValue(meterValue);
            meter.setRecordDate(recordDate); // อัปเดตวันที่ให้แน่ใจว่าตรงกัน
            electricMeterRepository.save(meter);
            System.out.println("✅ อัปเดตค่ามิเตอร์ไฟเรียบร้อย: " + meterValue);
        } else {
            // ✅ ถ้ายังไม่มีข้อมูล → บันทึกใหม่
            ElectricMeter newMeter = new ElectricMeter(room, meterValue, recordDate);
            electricMeterRepository.save(newMeter);
            System.out.println("✅ บันทึกมิเตอร์ไฟใหม่เรียบร้อย: " + meterValue);
        }
    }
    
    @Override
    public List<WaterMeter> getWaterMeterRecordsByRoomAndMonth(Long roomId, YearMonth month) {
        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();
        Room room = roomRepository.findById(roomId).orElse(null);
        if (room == null) {
            return Collections.emptyList(); // คืนลิสต์เปล่า
        }
        return waterMeterRepository.findByRoomAndRecordDateBetween(room, start, end);
    }

    @Override
    public List<ElectricMeter> getElectricMeterRecordsByRoomAndMonth(Long roomId, YearMonth month) {
        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();
        Room room = roomRepository.findById(roomId).orElse(null);
        if (room == null) {
            return Collections.emptyList(); // คืนลิสต์เปล่า
        }
        return electricMeterRepository.findByRoomAndRecordDateBetween(room, start, end);
    }

    @Override
    public List<ElectricMeter> getElectricMeterRecordsByMonth(YearMonth month) {
        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();
        return electricMeterRepository.findByRecordDateBetween(start, end);
    }

}
