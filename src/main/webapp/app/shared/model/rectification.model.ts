import { Moment } from 'moment';

export interface IRectification {
  id?: number;
  measure?: string;
  result?: string;
  rectificationTime?: Moment;
  remainsQuestionId?: number;
}

export class Rectification implements IRectification {
  constructor(
    public id?: number,
    public measure?: string,
    public result?: string,
    public rectificationTime?: Moment,
    public remainsQuestionId?: number
  ) {}
}
