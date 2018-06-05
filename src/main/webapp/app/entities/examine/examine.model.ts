import { BaseEntity } from './../../shared';

export class Examine implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public status?: string,
        public delFlag?: string,
        public departmentId?: number,
        public duration?: number,
        public score?: number,
        public projectId?: number,
    ) {
    }
}
