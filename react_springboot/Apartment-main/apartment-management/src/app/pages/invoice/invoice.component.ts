import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ApiService } from '../../shared/api.service';
import { MatTableModule } from '@angular/material/table';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { CommonModule } from '@angular/common';

export interface Invoice {
  roomPrice:number;
  billingMonth: string;
  roomId: number;
  roomNumber: string;
  cableFee: number;
  parkingFee: number;
  commonFee: number;
  waterCharge: number;
  electricCharge: number;
  waterMeterStart: number;
  waterMeterEnd: number;
  waterUsage: number;
  electricMeterStart: number;
  electricMeterEnd: number;
  electricUsage: number;
  totalAmount: number;
}

@Component({
  selector: 'app-invoice',
    standalone: true,
    imports: [
      MatTableModule, // ใช้สำหรับ <table mat-table>
      MatFormFieldModule,  // ✅ Import MatFormField
      MatSelectModule,  
      CommonModule   // ✅ Import MatSelect
    ],
  templateUrl: './invoice.component.html',
  styleUrls: ['./invoice.component.css'],
})
export class InvoiceComponent implements OnInit {
  invoices: Invoice[] = [];
  
 
  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
    this.fetchInvoices();
  }

  fetchInvoices(): void {
    this.apiService.getInvoices().subscribe(
      (data) => {
        this.invoices = data;
        console.log('📊 ข้อมูลที่โหลดจาก API:', this.invoices);
        
      },
      (error) => {
        console.error('❌ เกิดข้อผิดพลาดในการโหลดข้อมูล:', error);
      }
    );
  }
}
