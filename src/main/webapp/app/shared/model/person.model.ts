import { Moment } from 'moment';

export interface IPerson {
  id?: number;
  firstname?: string;
  lastname?: string;
  dob?: Moment;
  phone?: string;
  address?: string;
  email?: string;
  hotel?: string;
  datein?: Moment;
  dateout?: Moment;
  comments?: string;
}

export class Person implements IPerson {
  constructor(
    public id?: number,
    public firstname?: string,
    public lastname?: string,
    public dob?: Moment,
    public phone?: string,
    public address?: string,
    public email?: string,
    public hotel?: string,
    public datein?: Moment,
    public dateout?: Moment,
    public comments?: string
  ) {}
}
