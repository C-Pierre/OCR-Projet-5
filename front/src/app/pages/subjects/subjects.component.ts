import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { HeaderComponent } from '../../components/parts/common/header.component';
import { SubjectService } from 'src/app/core/services/subject.service';
import { Observable } from 'rxjs';
import { Subject } from 'src/app/core/models/subject.interface';

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
