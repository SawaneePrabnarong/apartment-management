import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../shared/api.service';
import { MatTableModule } from '@angular/material/table';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';

@Component({
  selector: 'app-summary',
  standalone: true,
  imports: [
    MatTableModule, // ใช้สำหรับ <table mat-table>
    MatFormFieldModule,  // ✅ Import MatFormField
    MatSelectModule,     // ✅ Import MatSelect
  ],
  templateUrl: './summary.component.html',
  styleUrls: ['./summary.component.css']
})
export class SummaryComponent implements OnInit {
  summaryData: any[] = [];
  // displayedColumns: string[] = ['room', 'water', 'electric', 'total'];
  displayedColumns: string[] = [
    'room', 'roomPrice', 
    'electricStart', 'electricEnd', 'electricUsage', 'electricCharge',
    'waterStart', 'waterEnd', 'waterUsage', 'waterCharge',
    'parkingFee', 'cableFee', 'commonFee', 'total', 'status'  
  ];
  selectedMonth: string = this.getCurrentMonth(); // ค่าเริ่มต้นเป็นเดือนปัจจุบัน
  //today = new Date().toLocaleDateString('th-TH', { year: 'numeric', month: 'long' });

  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
    this.fetchSummary();
  }

/** ดึงข้อมูลสรุปของเดือนที่เลือก */
fetchSummary(): void {
  console.log(`🔍 กำลังดึงข้อมูลของเดือน: ${this.selectedMonth}`);
  this.apiService.getSummary(this.selectedMonth).subscribe(
    (data) => {
      console.log("📊 ข้อมูลที่โหลดจาก API:", data);

      this.summaryData = data.map((item: any) => ({
        id:item.id,
        roomNumber: item.roomNumber,
        roomPrice: item.roomPrice ?? 0,
        electricStart: item.electricMeterStart ?? 0, 
        electricEnd: item.electricMeterEnd ?? 0, 
        electricUsage: item.electricUsage ?? 0, 
        electricCharge: item.electricBill ?? 0,  // ✅ ใช้ `electricBill`
        waterStart: item.waterMeterStart ?? 0, 
        waterEnd: item.waterMeterEnd ?? 0, 
        waterUsage: item.waterUsage ?? 0, 
        waterCharge: item.waterBill ?? 0,  // ✅ ใช้ `waterBill`
        parkingFee: item.parkingFee ?? 0, 
        cableFee: item.cableFee ?? 0, 
        commonFee: item.commonFee ?? 0, 
        total: item.totalBill ?? 0 , // ✅ ใช้ `totalBill`
        status: item.status ?? 'UNPAID' // ✅ เพิ่มสถานะเริ่มต้นเป็น UNPAID ถ้าไม่มีค่า

      }));

      console.log("📊 ข้อมูลที่แสดงใน UI:", this.summaryData);
    },
    (error) => {
      console.error("❌ เกิดข้อผิดพลาดในการโหลดข้อมูล:", error);
    }
  );
}
  /** เมื่อผู้ใช้เลือกเดือนใหม่ */
  onMonthChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.selectedMonth = input.value;
    this.apiService.generateBills(this.selectedMonth).subscribe(
      response => {
        console.log('✅ สร้างบิลสำเร็จ:', response);
        this.fetchSummary(); // โหลดข้อมูลใหม่
        
        
      },
      error => {
        console.error('❌ เกิดข้อผิดพลาดในการสร้างบิล:', error);
        alert('เกิดข้อผิดพลาดในการโหลดบิล!');
      }
    );
    
  }

  /** คืนค่าเดือนปัจจุบันในรูปแบบ YYYY-MM */
  private getCurrentMonth(): string {
    const today = new Date();
    return `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, '0')}`;
  }

  
  updateStatus(element: any, newStatus: string): void {
    console.log(`🔍 เปลี่ยนสถานะห้อง ${element.id} เป็น: ${newStatus}`);
  
    this.apiService.updateBillStatus(element.id, newStatus).subscribe(
      (response) => {
        console.log(`✅ อัปเดตสำเร็จ: ห้อง ${element.id} -> ${newStatus}`);
        element.status = newStatus; // อัปเดต UI
      },
      (error) => {
        console.error('❌ เกิดข้อผิดพลาดในการเปลี่ยนสถานะ:', error);
        alert('❌ ไม่สามารถอัปเดตสถานะได้!');
      }
    );
  }

  
}