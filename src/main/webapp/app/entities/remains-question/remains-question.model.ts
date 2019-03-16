import { BaseEntity } from './../../shared';

export const enum QuestionType {
    'LESS_MANAGE',
    'SECRET_RISK',
    'LEAKING_EVENT'
}

export class RemainsQuestion implements BaseEntity {
    constructor(
        public id?: number,
        public questionType?: QuestionType,
        public createdTime?: any,
        public description?: string,
        public reportItems?: BaseEntity,
    ) {
    }
}
