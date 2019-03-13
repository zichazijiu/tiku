import { BaseEntity } from './../../shared';

export class Department implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public code?: string,
        public parentId?: number,
        public departmentStatus?: string,
        public departmentType?: string,
        public delFlag?: string,
        public parentCodes?: string,
    ) {
    }
}
