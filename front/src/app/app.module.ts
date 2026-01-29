import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SubjectsComponent } from './pages/subjects/subjects.component';
import { HeaderComponent } from './components/parts/shared/header/header.component';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { CustomJwtInterceptor } from './core/interceptors/customJwtInterceptorFn';
import { ToastComponent } from './components/elements/shared/toast/toast.component';

@NgModule({
    declarations: [AppComponent],
    bootstrap: [AppComponent],
    imports: [
        BrowserModule,
        AppRoutingModule,
        BrowserAnimationsModule,
        SubjectsComponent,
        HeaderComponent,
        ToastComponent
    ],
        providers: [
            { provide: HTTP_INTERCEPTORS, useClass: CustomJwtInterceptor, multi: true },
        provideHttpClient(withInterceptorsFromDi())
    ] })
export class AppModule {}
