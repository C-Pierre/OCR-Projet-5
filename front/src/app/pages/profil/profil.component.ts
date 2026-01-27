import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { HeaderComponent } from 'src/app/components/parts/shared/header/header.component';
import { ButtonComponent } from 'src/app/components/elements/shared/button/button.component';

interface Subscription {
  id: number;
  title: string;
  description: string;
}

@Component({
  selector: 'app-profil',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, ButtonComponent, HeaderComponent],
  templateUrl: './profil.component.html',
  styleUrls: ['./profil.component.scss']
})
export class ProfilComponent implements OnInit {

  profileForm: FormGroup;
  subscriptions: Subscription[] = [
    {
      id: 1,
      title: 'Titre du thème 1',
      description: 'Description: lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry\'s standard...'
    },
    {
      id: 2,
      title: 'Titre du thème 2',
      description: 'Description: lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry\'s standard...'
    }
  ];

  constructor(private fb: FormBuilder) { 
    this.profileForm = this.fb.group({
      username: ['username'],
      email: ['email@email.fr'],
      password: ['']
    });
  }

  ngOnInit(): void { }

  saveProfile() {
    if (this.profileForm.valid) {
      console.log('Profil sauvegardé', this.profileForm.value);
      // ici appeler un service pour sauvegarder
    }
  }

  unsubscribe(subId: number) {
    console.log('Se désabonner de', subId);
    // ici appeler un service pour se désabonner
  }
}
