import { BaseEntity } from './../../shared';

export const enum DeleteFlag {
    'NORMAL',
    'DELETE'
}

export const enum Status {
    'NORMAL'
}

export const enum Type {
    'NORMAL'
}

export class Project implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public delFlag?: DeleteFlag,
        public status?: Status,
        public type?: Type,
    ) {
    }
}
