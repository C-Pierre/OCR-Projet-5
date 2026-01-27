import { Observable } from 'rxjs';
import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { Subject } from 'src/app/core/models/subject/subject.interface';
import { SubjectService } from 'src/app/core/services/subject/subject.service';
import { HeaderComponent } from '../../components/parts/shared/header/header.component';

@Component({
  selector: 'app-subjects',
  standalone: true,
  imports: [
    CommonModule,
    HeaderComponent,
    MatButtonModule
  ],
  templateUrl: './subjects.component.html',
  styleUrls: ['./subjects.component.scss']
})
export class SubjectsComponent {

  private subjectService = inject(SubjectService);

  public themes: Observable<Subject[]> = this.subjectService.all();

  subscribe(theme: Subject) {
    theme.subscribed = true;
  }

  trackById(index: number, item: Subject) {
    return item.id;
  }
}