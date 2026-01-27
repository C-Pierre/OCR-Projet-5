import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { SubjectsComponent } from './pages/subjects/subjects.component';
import { RegisterComponent } from './pages/register/register.component';

const routes: Routes = [
  { path: '',  component: HomeComponent },
  { path: 'themes', component: SubjectsComponent },
  { path: 'login', component: LoginComponent },
  { path: 'signin', component: RegisterComponent },
  { path: '', redirectTo: '/signin', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}