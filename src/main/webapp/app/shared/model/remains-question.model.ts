import { Moment } from 'moment';

export const enum QuestionType {
  LESS_MANAGE = 'LESS_MANAGE',
  SECRET_RISK = 'SECRET_RISK',
  LEAKING_EVENT = 'LEAKING_EVENT'
}

export interface IRemainsQuestion {
  id?: number;
  questionType?: QuestionType;
  createdTime?: Moment;
  description?: string;
  itemAnswerId?: number;
}

export class RemainsQuestion implements IRemainsQuestion {
  constructor(
    public id?: number,
    public questionType?: QuestionType,
    public createdTime?: Moment,
    public description?: string,
    public itemAnswerId?: number
  ) {}
}
