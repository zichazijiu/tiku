import { BaseEntity } from './../../shared';

export class Statistics implements BaseEntity {
    constructor(
        public id?: number,
        public type?: string,
        public statisticsDate?: any,
        public updateDate?: any,
        public count?: number,
        public name?: string,
    ) {
    }
}
