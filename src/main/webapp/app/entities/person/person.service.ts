import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPerson } from 'app/shared/model/person.model';

type EntityResponseType = HttpResponse<IPerson>;
type EntityArrayResponseType = HttpResponse<IPerson[]>;

@Injectable({ providedIn: 'root' })
export class PersonService {
  public resourceUrl = SERVER_API_URL + 'api/people';

  constructor(protected http: HttpClient) {}

  create(person: IPerson): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(person);
    return this.http
      .post<IPerson>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(person: IPerson): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(person);
    return this.http
      .put<IPerson>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPerson>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPerson[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(person: IPerson): IPerson {
    const copy: IPerson = Object.assign({}, person, {
      dob: person.dob && person.dob.isValid() ? person.dob.format(DATE_FORMAT) : undefined,
      datein: person.datein && person.datein.isValid() ? person.datein.format(DATE_FORMAT) : undefined,
      dateout: person.dateout && person.dateout.isValid() ? person.dateout.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dob = res.body.dob ? moment(res.body.dob) : undefined;
      res.body.datein = res.body.datein ? moment(res.body.datein) : undefined;
      res.body.dateout = res.body.dateout ? moment(res.body.dateout) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((person: IPerson) => {
        person.dob = person.dob ? moment(person.dob) : undefined;
        person.datein = person.datein ? moment(person.datein) : undefined;
        person.dateout = person.dateout ? moment(person.dateout) : undefined;
      });
    }
    return res;
  }
}
