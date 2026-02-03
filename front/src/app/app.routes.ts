import { Routes } from '@angular/router';
import { AuthGuard } from './core/guards/auth.guard';
import { UnauthGuard } from './core/guards/unauth.guard';
import { HomeComponent } from './pages/home/home.component';
import { FeedComponent } from './pages/feed/feed.component';
import { LoginComponent } from './pages/login/login.component';
import { ProfilComponent } from './pages/profil/profil.component';
import { SubjectsComponent } from './pages/subjects/subjects.component';
import { RegisterComponent } from './pages/register/register.component';
import { PostDetailComponent } from './pages/post/detail/post-detail.component';
import { PostCreateComponent } from './pages/post/create/post-create.component';

export const routes: Routes = [
  { path: '',  component: HomeComponent },
  { path: 'feed', component: FeedComponent, canActivate: [AuthGuard] },
  {
    path: 'posts',
    canActivate: [AuthGuard],
    children: [
      { path: 'create', component: PostCreateComponent },
      { path: ':id', component: PostDetailComponent }
    ]
  },
  { path: 'themes', component: SubjectsComponent },
  { path: 'profil', component: ProfilComponent, canActivate: [AuthGuard] },
  { path: 'login', component: LoginComponent, canActivate: [UnauthGuard] },
  { path: 'signin', component: RegisterComponent, canActivate: [UnauthGuard] },
];