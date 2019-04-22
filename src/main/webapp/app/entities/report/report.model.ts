import { BaseEntity, User } from './../../shared';

export const enum ReportStatus {
    'FINISH',
    'HALT',
    'NEW',
    'RESET'
}

export class Report implements BaseEntity {
    constructor(
        public id?: number,
        public createdTime?: any,
        public reportStatus?: ReportStatus,
        public level?: string,
        public checkItemsReleaseId?: number,
        public reportName?: string,
        public user?: User,
        public reportItems?: BaseEntity[],
    ) {
    }
}
