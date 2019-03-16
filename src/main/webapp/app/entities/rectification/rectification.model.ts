import { BaseEntity } from './../../shared';

export class Rectification implements BaseEntity {
    constructor(
        public id?: number,
        public measure?: string,
        public result?: string,
        public rectificationTime?: any,
    ) {
    }
}
