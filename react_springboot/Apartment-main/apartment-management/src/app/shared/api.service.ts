//โค้ดเก่าแนนด้านบนเทสเจนมา
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private baseUrl = 'http://localhost:8080/api';
  private userInfoSubject = new BehaviorSubject<any>(null);
  userInfo$ = this.userInfoSubject.asObservable();

  constructor(private http: HttpClient) {}
  login(data: any): Observable<any> {
    return new Observable(observer => {
      this.http.post(`${this.baseUrl}/auth/login`, data).subscribe(
        (response: any) => {
          localStorage.setItem('token', response.token);
          localStorage.setItem('userInfo', JSON.stringify(response.user));
          this.userInfoSubject.next(response.user); // ✅ อัปเดต userInfo
          observer.next(response);
          observer.complete();
        },
        (error) => observer.error(error)
      );
    });
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('userInfo');
    this.userInfoSubject.next(null); // ✅ อัปเดตค่าให้เป็น null
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  isLoggedIn(): boolean {
    return !!this.getToken(); // ✅ ตรวจสอบว่ามี Token หรือไม่
  }

  getRooms(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/rooms`);
  }

  saveWaterMeterData(data: any): Observable<any> {
    console.log('📢 Data ที่กำลังส่งไปยัง API:', JSON.stringify(data, null, 2));

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.post(`${this.baseUrl}/meters/water`, data, {
      headers,
      responseType: 'text',
    });
  }

  saveElectricMeterData(data: any): Observable<any> {
    console.log('📢 Data ที่กำลังส่งไปยัง API:', JSON.stringify(data, null, 2));

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.post(`${this.baseUrl}/meters/electric`, data, {
      headers,
      responseType: 'text',
    });
  }

  generateBills(month: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/billing/generate/${month}`, {});
}


  getSummary(month: string): Observable<any> {
    //return this.http.get(`${this.baseUrl}/summary`);
    return this.http.get(`${this.baseUrl}/billing/summary?month=${month}`);
  }

  getBilling(): Observable<any> {
    return this.http.get(`${this.baseUrl}/billing`);
  }
}
