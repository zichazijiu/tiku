import { BaseEntity } from './../../shared';

export class ReleaseHistory implements BaseEntity {
    constructor(
        public id?: number,
        public releaseId?: number,
    ) {
    }
}
