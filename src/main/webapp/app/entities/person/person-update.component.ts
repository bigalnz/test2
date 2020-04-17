import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPerson, Person } from 'app/shared/model/person.model';
import { PersonService } from './person.service';

@Component({
  selector: 'jhi-person-update',
  templateUrl: './person-update.component.html'
})
export class PersonUpdateComponent implements OnInit {
  isSaving = false;
  dobDp: any;
  dateinDp: any;
  dateoutDp: any;

  editForm = this.fb.group({
    id: [],
    firstname: [null, [Validators.required, Validators.minLength(2)]],
    lastname: [null, [Validators.required, Validators.minLength(2)]],
    dob: [],
    phone: [],
    address: [],
    email: [],
    hotel: [],
    datein: [],
    dateout: [],
    comments: []
  });

  constructor(protected personService: PersonService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ person }) => {
      this.updateForm(person);
    });
  }

  updateForm(person: IPerson): void {
    this.editForm.patchValue({
      id: person.id,
      firstname: person.firstname,
      lastname: person.lastname,
      dob: person.dob,
      phone: person.phone,
      address: person.address,
      email: person.email,
      hotel: person.hotel,
      datein: person.datein,
      dateout: person.dateout,
      comments: person.comments
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const person = this.createFromForm();
    if (person.id !== undefined) {
      this.subscribeToSaveResponse(this.personService.update(person));
    } else {
      this.subscribeToSaveResponse(this.personService.create(person));
    }
  }

  private createFromForm(): IPerson {
    return {
      ...new Person(),
      id: this.editForm.get(['id'])!.value,
      firstname: this.editForm.get(['firstname'])!.value,
      lastname: this.editForm.get(['lastname'])!.value,
      dob: this.editForm.get(['dob'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      address: this.editForm.get(['address'])!.value,
      email: this.editForm.get(['email'])!.value,
      hotel: this.editForm.get(['hotel'])!.value,
      datein: this.editForm.get(['datein'])!.value,
      dateout: this.editForm.get(['dateout'])!.value,
      comments: this.editForm.get(['comments'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPerson>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
