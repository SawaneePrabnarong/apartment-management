package com.apartment.management.service;

import java.util.List;

import com.apartment.management.dto.BillingDTO;
import com.apartment.management.model.Billing;

public interface BillingService {

    // สร้างใบแจ้งหนี้สำหรับทุกห้องในเดือนปัจจุบัน
    List<Billing> generateBillingForCurrentMonth(String month);

    // ดึงข้อมูลใบแจ้งหนี้ของห้องทั้งหมดในเดือนปัจจุบัน
    List<Billing> getBillingRecordsForCurrentMonth();

    // คำนวณค่าใช้จ่าย (มิเตอร์น้ำและไฟ) สำหรับห้องตามเดือน
    Billing calculateBillingForRoom(Long roomId, String month);

    // สรุปค่าใช้จ่ายทั้งหมดในเดือนนี้ (บิลรวม)
    List<Billing> getBillingSummaryByMonth(String month);

    Billing updateBillingStatus(Long billingId, String status);
    Billing createBilling(Billing billing);

    List<BillingDTO> getAllInvoices();

    List<BillingDTO> getInvoicesByMonth(String month);
    List<BillingDTO> getInvoicesForCurrentMonth();  

   
}