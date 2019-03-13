import { BaseEntity } from './../../shared';

export class CheckItemAnswer implements BaseEntity {
    constructor(
        public id?: number,
        public itemId?: number,
        public result?: string,
        public createdBy?: string,
        public deptId?: number,
        public createdDate?: any,
    ) {
    }
}
