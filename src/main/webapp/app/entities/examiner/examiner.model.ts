import { BaseEntity } from './../../shared';

export class Examiner implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public departmentId?: number,
        public userId?: number,
        public time?: number,
    ) {
    }
}
