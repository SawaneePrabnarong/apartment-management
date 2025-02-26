package com.apartment.management.service.impl;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apartment.management.dto.BillingDTO;
import com.apartment.management.model.Billing;
import com.apartment.management.model.ElectricMeter;
import com.apartment.management.model.Room;
import com.apartment.management.model.WaterMeter;
import com.apartment.management.repository.BillingRepository;
import com.apartment.management.repository.FloorPriceRepository;
import com.apartment.management.repository.RoomRepository;
import com.apartment.management.service.BillingService;

@Service
public class BillingServiceImpl implements BillingService {

    @Autowired
    private BillingRepository billingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private FloorPriceRepository floorPriceRepository;

    @Override
    public List<Billing> generateBillingForCurrentMonth(String month) {
        YearMonth targetMonth = YearMonth.parse(month); // ✅ รับค่า `month` จาก UI
        YearMonth previousMonth = targetMonth.minusMonths(1); // ✅ หาค่าเดือนก่อนหน้า
        String monthString = targetMonth.toString();
        String previousMonthString = previousMonth.toString(); // ✅ ใช้หาค่ามิเตอร์ก่อนหน้า

        System.out.println("🔍 สร้างบิลของเดือน: " + monthString + " (ใช้มิเตอร์จาก " + previousMonthString + ")");

        List<Room> rooms = roomRepository.findAll();
        List<Billing> updatedBillings = new ArrayList<>();

        for (Room room : rooms) {
            List<Billing> existingBillings = billingRepository.findByRoomAndMonth(room, monthString);

            Billing billing;
            if (!existingBillings.isEmpty()) {
                billing = existingBillings.get(0);
            } else {
                billing = new Billing();
                billing.setRoom(room);
                billing.setRoomNumber(room.getRoomNumber());
                billing.setMonth(monthString);
            }

            // ✅ ดึงค่าห้องจาก FloorPriceRepository ตามชั้นที่กำหนด
            BigDecimal roomPrice = floorPriceRepository.findById(room.getFloor())
                    .map(floor -> BigDecimal.valueOf(floor.getPrice()))
                    .orElseThrow(() -> new RuntimeException("Floor price not found"));

            // ✅ ดึงค่ามิเตอร์ก่อนหน้าโดยใช้ `previousMonth`
            double lastWaterMeter = room.getWaterMeters().stream()
                    .filter(meter -> YearMonth.from(meter.getRecordDate()).equals(previousMonth))
                    .map(WaterMeter::getMeterValue)
                    .reduce((first, second) -> second)
                    .orElse(0.0);

            double currentWaterMeter = room.getWaterMeters().stream()
                    .filter(meter -> YearMonth.from(meter.getRecordDate()).equals(targetMonth))
                    .map(WaterMeter::getMeterValue)
                    .reduce((first, second) -> second)
                    .orElse(0.0);
            // .orElse(lastWaterMeter); // ✅ ใช้ค่าเดือนก่อนหน้าถ้าไม่มีค่าปัจจุบัน

            double lastElectricMeter = room.getElectricMeters().stream()
                    .filter(meter -> YearMonth.from(meter.getRecordDate()).equals(previousMonth))
                    .map(ElectricMeter::getMeterValue)
                    .reduce((first, second) -> second)
                    .orElse(0.0);

            double currentElectricMeter = room.getElectricMeters().stream()
                    .filter(meter -> YearMonth.from(meter.getRecordDate()).equals(targetMonth))
                    .map(ElectricMeter::getMeterValue)
                    .reduce((first, second) -> second)
                    .orElse(0.0);
            // .orElse(lastElectricMeter); // ✅ ใช้ค่าเดือนก่อนหน้าถ้าไม่มีค่าปัจจุบัน

            // ✅ ใช้ฟังก์ชันป้องกันมิเตอร์รีเซ็ต
            double waterUsage = calculateMeterUsage(lastWaterMeter, currentWaterMeter);
            double electricUsage = calculateMeterUsage(lastElectricMeter, currentElectricMeter);

            BigDecimal waterBill = BigDecimal.valueOf(waterUsage * 25);
            BigDecimal electricBill = BigDecimal.valueOf(electricUsage * 7);
            BigDecimal defaultParkingFee = BigDecimal.valueOf(400);
            BigDecimal defaultCableFee = BigDecimal.valueOf(100);
            BigDecimal defaultCommonFee = BigDecimal.valueOf(200);
            // ✅ รวมค่าใช้จ่ายทั้งหมด
            BigDecimal totalBill = roomPrice.add(waterBill).add(electricBill)
                    .add(defaultParkingFee)
                    .add(defaultCableFee)
                    .add(defaultCommonFee);
            // BigDecimal totalBill = waterBill.add(electricBill)
            // .add(defaultParkingFee)
            // .add(defaultCableFee)
            // .add(defaultCommonFee);

            System.out.println("💰 รวมค่าใช้จ่ายห้อง " + room.getRoomNumber() + " = " + totalBill);

            billing.setRoomPrice(roomPrice);
            billing.setParkingFee(defaultParkingFee);
            billing.setCableFee(defaultCableFee);
            billing.setCommonFee(defaultCommonFee);

            billing.setWaterMeterStart(lastWaterMeter);
            billing.setWaterMeterEnd(currentWaterMeter);
            billing.setElectricMeterStart(lastElectricMeter);
            billing.setElectricMeterEnd(currentElectricMeter);
            billing.setWaterUsage(waterUsage);
            billing.setElectricUsage(electricUsage);
            billing.setWaterBill(waterBill);
            billing.setElectricBill(electricBill);
            billing.setTotalBill(totalBill);

            billingRepository.save(billing);
            updatedBillings.add(billing);
        }

        return updatedBillings;
    }

    @Override
    public List<Billing> getBillingRecordsForCurrentMonth() {
        // ✅ ดึงข้อมูลบิลของทุกห้องในเดือนปัจจุบัน
        return billingRepository.findAll();
    }

    @Override
    public Billing calculateBillingForRoom(Long roomId, String month) {
        // ✅ คำนวณค่าใช้จ่ายของห้องตามเดือนที่ระบุ
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        List<Billing> billings = billingRepository.findByRoomAndMonth(room, month);

        if (billings.isEmpty()) {
            return null; // ถ้าไม่มีบิลในเดือนนั้น
        }

        return billings.get(0);
    }

    @Override
    public List<Billing> getBillingSummaryByMonth(String month) {
        // ✅ ดึงบิลของทุกห้องในเดือนที่ระบุ
        return billingRepository.findByMonth(month);
    }

    // ✅ ฟังก์ชันรองรับมิเตอร์ที่วนกลับไป 0000
    private double calculateMeterUsage(double lastMeter, double currentMeter) {
        return (currentMeter < lastMeter) ? (9999 - lastMeter) + currentMeter + 1 : currentMeter - lastMeter;
    }

    @Override
    public Billing updateBillingStatus(Long billingId, String status) {
        return billingRepository.findById(billingId).map(billing -> {
            billing.setStatus(status);
            return billingRepository.save(billing);
        }).orElse(null); // ✅ ถ้าไม่เจอบิลให้ return null (ป้องกัน error)
    }

    @Override
    public Billing createBilling(Billing billing) {
        if (billing.getStatus() == null || billing.getStatus().isEmpty()) {
            billing.setStatus("Unpaid"); // ตั้งค่าเริ่มต้น
        }
        return billingRepository.save(billing);
    }

    // private final BillingRepository billingRepository;

    @Override
    public List<BillingDTO> getAllInvoices() {
        List<Billing> billings = billingRepository.findAllOrderByRoomNumber();
         System.out.println(billings);
        return billings.stream().map(BillingDTO::new).collect(Collectors.toList());
    }

}