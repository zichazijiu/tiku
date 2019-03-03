import { BaseEntity } from './../../shared';

export class CheckItemAnswer implements BaseEntity {
    constructor(
        public id?: number,
        public itemId?: number,
        public yiliuProblems?: string,
        public zhenggaiInfo?: string,
        public result?: string,
        public createdBy?: string,
        public createdDate?: any,
    ) {
    }
}
