import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule, FormBuilder } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../shared/api.service';
import { HttpClientModule, HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule,HttpClientModule], // ✅ ต้อง import ReactiveFormsModule
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm: FormGroup;
  errorMessage: string = '';
  constructor(
    private fb: FormBuilder,
    private apiService: ApiService,  // ✅ ตรวจสอบให้แน่ใจว่า ApiService ถูก Inject
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }



  onSubmit() {
    if (this.loginForm.invalid) {
      return;
    }

    this.apiService.login(this.loginForm.value).subscribe({
      next: (response: any) => {
        console.log('✅ Login Success:', response);
        
        localStorage.setItem('token', response.token);  // ✅ เก็บ Token ไว้ใน LocalStorage
        localStorage.setItem('userInfo',JSON.stringify(this.loginForm.value));

        this.router.navigate(['/water-meter']);  // ✅ นำทางไปหน้า Dashboard
      },
      error: (error: HttpErrorResponse) => {
        console.error('❌ Login Failed:', error);
        this.errorMessage = 'ชื่อผู้ใช้หรือรหัสผ่านไม่ถูกต้อง';
        alert(this.errorMessage);
        this.loginForm.reset();
        
      }
    });
    
  }
}
