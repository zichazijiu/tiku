import { BaseEntity } from './../../shared';

export class Subject implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public title?: string,
        public description?: string,
        public status?: string,
        public type?: string,
        public right?: number,
        public delFlag?: string,
    ) {
    }
}
