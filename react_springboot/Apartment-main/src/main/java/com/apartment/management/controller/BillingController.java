package com.apartment.management.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apartment.management.dto.BillingDTO;
import com.apartment.management.model.Billing;
import com.apartment.management.service.BillingService;
import com.apartment.management.service.MeterService;

@RestController
@RequestMapping("/api/billing")
public class BillingController {

    @Autowired
    private BillingService billingService;

    @Autowired
    private MeterService meterService;

    // สร้างใบแจ้งหนี้สำหรับทุกห้องในเดือนปัจจุบัน
    @PostMapping("/generate/{month}")
    public ResponseEntity<List<Billing>> generateBillsForCurrentMonth(@PathVariable String month) {
        System.out.println("APIIIIIIIIIIIIIIIIIIIIIIII: /api/billing/generate/" + month);
        List<Billing> billings = billingService.generateBillingForCurrentMonth(month);
        System.out.println("billinggggggggggggggg" + billings);
        if (billings.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(billings, HttpStatus.CREATED);
    }

    // ดึงข้อมูลใบแจ้งหนี้ของห้องทั้งหมดในเดือนปัจจุบัน
    @GetMapping("/current")
    public ResponseEntity<List<Billing>> getCurrentMonthBills() {
        List<Billing> billings = billingService.getBillingRecordsForCurrentMonth();
        if (billings.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(billings, HttpStatus.OK);
    }

    // คำนวณค่าใช้จ่าย (มิเตอร์น้ำและไฟ) สำหรับห้องตามเดือน
    @GetMapping("/{roomId}/{month}")
    public ResponseEntity<Billing> getBillingForRoomByMonth(
            @PathVariable Long roomId,
            @PathVariable String month) {
        Billing billing = billingService.calculateBillingForRoom(roomId, month);
        if (billing == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(billing, HttpStatus.OK);
    }

    // สรุปค่าใช้จ่ายทั้งหมดในเดือนนี้ (บิลรวม)
    @GetMapping("/summary")
    public ResponseEntity<List<Billing>> getSummary(@RequestParam String month) {
        List<Billing> billings = billingService.getBillingSummaryByMonth(month);
        return ResponseEntity.ok(billings);
    }

    @GetMapping("/summary/{month}")
    public ResponseEntity<List<BillingDTO>> getBillingSummaryByMonth(@PathVariable String month) {
        List<Billing> billings = billingService.getBillingSummaryByMonth(month);

        if (billings.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // ✅ แปลง `Billing` เป็น `BillingDTO` 
        List<BillingDTO> billingDTOs = billings.stream()
                .map(BillingDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(billingDTOs);
    }

    @PatchMapping("/update-status/{billingId}")
    public ResponseEntity<Billing> updateBillingStatus(
            @PathVariable Long billingId,
            @RequestParam String status) {

        Billing updatedBilling = billingService.updateBillingStatus(billingId, status);
        if (updatedBilling == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(updatedBilling);
    }

    @PostMapping
    public ResponseEntity<Billing> createBilling(@RequestBody Billing billing) {
        Billing savedBilling = billingService.createBilling(billing);
        return new ResponseEntity<>(savedBilling, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updatePaymentStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        if (request == null || !request.containsKey("status")) {
            return ResponseEntity.badRequest().body("Missing 'status' field in request");
        }
    
        String newStatus = request.get("status");
    
        if (!"PAID".equals(newStatus) && !"UNPAID".equals(newStatus)) {
            return ResponseEntity.badRequest().body("Invalid status value");
        }
    
        Billing updatedBilling = billingService.updateBillingStatus(id, newStatus);
        if (updatedBilling == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    
        return ResponseEntity.ok(updatedBilling);
    }

    @GetMapping("/invoices")
    public ResponseEntity<List<BillingDTO>> getAllInvoices() {
        List<BillingDTO> invoices = billingService.getAllInvoices();
        return ResponseEntity.ok(invoices);
    }
    

}