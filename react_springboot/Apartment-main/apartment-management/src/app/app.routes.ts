import { Routes } from '@angular/router';
import { WaterMeterComponent } from './pages/water-meter/water-meter.component';
import { ElectricMeterComponent } from './pages/electric-meter/electric-meter.component';
import { SummaryComponent } from './pages/summary/summary.component';
import { BillingComponent } from './pages/billing/billing.component';
import { LayoutComponent } from './shared/layout/layout.component';
import { LoginComponent } from './pages/login/login.component';
import { AuthGuard } from './guards/auth.guard'; // ✅ Import Guard
import { InvoiceComponent } from './pages/invoice/invoice.component';

export const routes: Routes = [
    {
      path: '',
      component: LayoutComponent,
      children: [
        { path: 'water-meter', component: WaterMeterComponent , canActivate: [AuthGuard] },
        { path: 'electric-meter', component: ElectricMeterComponent , canActivate: [AuthGuard]},
        { path: 'summary', component: SummaryComponent , canActivate: [AuthGuard]},
        { path: 'billing', component: BillingComponent , canActivate: [AuthGuard]},
        { path: 'login', component: LoginComponent },
        { path: 'invoice', component: InvoiceComponent , canActivate: [AuthGuard]}, // เพิ่ม Route ใหม่
        { path: '', redirectTo: 'login', pathMatch: 'full' },
      ],
    },
  ];

