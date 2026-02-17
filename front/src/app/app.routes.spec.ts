import { routes } from './app.routes';
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

describe('App Routes', () => {
  const findRoute = (path: string) =>
    routes.find(route => route.path === path);

  it('should define home route', () => {
    const route = findRoute('');
    expect(route).toBeDefined();
    expect(route?.component).toBe(HomeComponent);
  });

  it('should define feed route protected by AuthGuard', () => {
    const route = findRoute('feed');
    expect(route?.component).toBe(FeedComponent);
    expect(route?.canActivate).toContain(AuthGuard);
  });

  it('should define subjects route', () => {
    const route = findRoute('themes');
    expect(route?.component).toBe(SubjectsComponent);
  });

  it('should define profil route protected by AuthGuard', () => {
    const route = findRoute('profil');
    expect(route?.component).toBe(ProfilComponent);
    expect(route?.canActivate).toContain(AuthGuard);
  });

  it('should define login route protected by UnauthGuard', () => {
    const route = findRoute('login');
    expect(route?.component).toBe(LoginComponent);
    expect(route?.canActivate).toContain(UnauthGuard);
  });

  it('should define register route protected by UnauthGuard', () => {
    const route = findRoute('signin');
    expect(route?.component).toBe(RegisterComponent);
    expect(route?.canActivate).toContain(UnauthGuard);
  });

  describe('posts routes', () => {
    const postsRoute = findRoute('posts');

    it('should exist and be protected by AuthGuard', () => {
      expect(postsRoute).toBeDefined();
      expect(postsRoute?.canActivate).toContain(AuthGuard);
      expect(postsRoute?.children).toBeDefined();
    });

    it('should define create post route', () => {
      const createRoute = postsRoute?.children?.find(r => r.path === 'create');
      expect(createRoute?.component).toBe(PostCreateComponent);
    });

    it('should define post detail route with id param', () => {
      const detailRoute = postsRoute?.children?.find(r => r.path === ':id');
      expect(detailRoute?.component).toBe(PostDetailComponent);
    });
  });

  it('should not have duplicate paths at root level', () => {
    const paths = routes.map(r => r.path);
    const uniquePaths = new Set(paths);
    expect(uniquePaths.size).toBe(paths.length);
  });
});
