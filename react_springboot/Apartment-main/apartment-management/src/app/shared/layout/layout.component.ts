import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { Router, RouterModule, NavigationEnd } from '@angular/router';
import { CommonModule } from '@angular/common';
import { filter } from 'rxjs/operators';
import { ApiService } from '../../shared/api.service'; // ✅ นำเข้า ApiService
import { BehaviorSubject, Observable } from 'rxjs';

interface UserInfo {
  username: string;
}


@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatSidenavModule,
    MatToolbarModule,
    MatIconModule,
    MatListModule,
  ],
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.css'],
})
export class LayoutComponent implements OnInit {
  @ViewChild('sidenav') sidenav!: MatSidenav;
  private userInfoSubject = new BehaviorSubject<UserInfo | null>(null);
  userInfo$: Observable<UserInfo | null> = this.userInfoSubject.asObservable(); // ✅ ใช้ Observable เพื่อให้ Angular ติดตามค่าตลอด

  constructor(
    private router: Router,
    private apiService: ApiService,
    private cdRef: ChangeDetectorRef // ✅ ใช้เพื่อตรวจจับการเปลี่ยนแปลง UI
  ) {}

  ngOnInit() {
    this.loadUserInfo(); // ✅ โหลดข้อมูลผู้ใช้เมื่อ Component เริ่มต้น

    // ✅ ตรวจจับเมื่อเปลี่ยน Route และโหลด userInfo ใหม่
    this.router.events.pipe(filter((event) => event instanceof NavigationEnd)).subscribe(() => {
      this.loadUserInfo();
      this.cdRef.detectChanges(); // ✅ บังคับให้ UI อัปเดต
    });
  }

  /** ✅ โหลดข้อมูลจาก LocalStorage */
  loadUserInfo() {
    const storedUserInfo = localStorage.getItem('userInfo');
    if (storedUserInfo) {
      this.userInfoSubject.next(JSON.parse(storedUserInfo) as UserInfo);
    } else {
      this.userInfoSubject.next(null);
    }
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('userInfo');
    this.userInfoSubject.next(null); // ✅ อัปเดตค่าให้เป็น null
    this.router.navigate(['/login']);
  }

  isLoginPage(): boolean {
    return this.router.url === '/login' || this.router.url === '/';
  }

  isLoggedIn(): boolean {
    return this.apiService.isLoggedIn();
  }
  handleSummaryClick(): void {
        console.log('🔄 กำลังสร้างบิล...');
        const currentMonth = new Date().toISOString().slice(0, 7);
        //this.router.navigate(['/summary']);
    
        // ✅ ยิง API `generateBills()`
        this.apiService.generateBills(currentMonth).subscribe(
          response => {
            console.log('✅ สร้างบิลสำเร็จ:', response);
            
            // ✅ หลังจากยิง API เสร็จแล้วให้เปลี่ยนหน้าไป "/summary"
            this.router.navigate(['/summary']);
          },
          error => {
            console.error('❌ เกิดข้อผิดพลาดในการสร้างบิล:', error);
            alert('เกิดข้อผิดพลาดในการโหลดบิล!');
          }
        );
      }
}

// import { Component, OnInit, ViewChild } from '@angular/core';
// import { MatSidenav } from '@angular/material/sidenav';
// import { MatToolbarModule } from '@angular/material/toolbar';
// import { MatIconModule } from '@angular/material/icon';
// import { MatSidenavModule } from '@angular/material/sidenav';
// import { MatListModule } from '@angular/material/list';
// import { Router, RouterModule, NavigationEnd } from '@angular/router';
// import { CommonModule } from '@angular/common';
// import { filter } from 'rxjs/operators';
// import { ApiService } from '../../shared/api.service'; // ✅ นำเข้า ApiService

// interface UserInfo {
//   username: string;
// }


// @Component({
//   selector: 'app-layout',
//   standalone: true,
//   imports: [
//     CommonModule,
//     RouterModule,
//     MatSidenavModule,
//     MatToolbarModule,
//     MatIconModule,
//     MatListModule,
//   ],
//   templateUrl: './layout.component.html',
//   styleUrls: ['./layout.component.css'],
// })
// export class LayoutComponent implements OnInit {
//   @ViewChild('sidenav') sidenav!: MatSidenav;

//   constructor(private router: Router, private apiService: ApiService) {
//     this.router.events
//       .pipe(filter((event) => event instanceof NavigationEnd))
//       .subscribe(() => {
//         if (this.sidenav) {
//           this.sidenav.close();
//         }
//       });
//   }
//   userInfo: UserInfo | null = null; // ✅ กำหนด Type อย่างชัดเจน

 

//   ngOnInit() {
//     // ✅ ตรวจสอบและแปลงข้อมูลจาก LocalStorage
//     const storedUserInfo = localStorage.getItem('userInfo');
//     if (storedUserInfo) {
//       this.userInfo = JSON.parse(storedUserInfo) as UserInfo;
//     }
//   }

//   logout() {
//     localStorage.removeItem('token');
//     localStorage.removeItem('userInfo');
//     this.userInfo = null;
//     this.router.navigate(['/login']);
//   }
  
//   /** ✅ ตรวจสอบว่าขณะนี้อยู่ที่หน้า login หรือไม่ */
//   isLoginPage(): boolean {
//     console.log(this.router.url);
//     return this.router.url === '/login' || this.router.url === '/';
//   }

//   isLoggedIn(): boolean {
//     return this.apiService.isLoggedIn();
//   }


//   // logout(): void {
//   //   this.apiService.logout();
//   //   this.router.navigate(['/login']);
//   // }

//   /** ✅ ยิง API และเปลี่ยนหน้า */
//   handleSummaryClick(): void {
//     console.log('🔄 กำลังสร้างบิล...');

//     // ✅ ยิง API `generateBills()`
//     this.apiService.generateBills().subscribe(
//       response => {
//         console.log('✅ สร้างบิลสำเร็จ:', response);
        
//         // ✅ หลังจากยิง API เสร็จแล้วให้เปลี่ยนหน้าไป "/summary"
//         this.router.navigate(['/summary']);
//       },
//       error => {
//         console.error('❌ เกิดข้อผิดพลาดในการสร้างบิล:', error);
//         alert('เกิดข้อผิดพลาดในการโหลดบิล!');
//       }
//     );
//   }
// }
