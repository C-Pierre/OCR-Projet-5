import { NgModule } from '@angular/core';
import { AuthGuard } from './core/guards/auth.guard';
import { UnauthGuard } from './core/guards/unauth.guard';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { FeedComponent } from './pages/feed/feed.component';
import { LoginComponent } from './pages/login/login.component';
import { ProfilComponent } from './pages/profil/profil.component';
import { SubjectsComponent } from './pages/subjects/subjects.component';
import { RegisterComponent } from './pages/register/register.component';

const routes: Routes = [
  { path: '',  component: HomeComponent },
  { path: 'feed', component: FeedComponent},
  { path: 'themes', component: SubjectsComponent },
  { path: 'profil', component: ProfilComponent, canActivate: [AuthGuard] },
  { path: 'login', component: LoginComponent, canActivate: [UnauthGuard] },
  { path: 'signin', component: RegisterComponent, canActivate: [UnauthGuard] },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}