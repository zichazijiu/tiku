import { BaseEntity } from './../../shared';

export const enum ReleaseStatus {
    'TEST',
    'RELEASE',
    'CLOSE'
}

export class Release implements BaseEntity {
    constructor(
        public id?: number,
        public checkItemIds?: string,
        public releaseUser?: string,
        public releaseStatus?: ReleaseStatus,
    ) {
    }
}
