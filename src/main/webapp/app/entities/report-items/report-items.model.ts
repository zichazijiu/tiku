import { BaseEntity } from './../../shared';

export class ReportItems implements BaseEntity {
    constructor(
        public id?: number,
        public level?: string,
        public report?: BaseEntity,
        public checkItem?: BaseEntity,
        public remainsQuestions?: BaseEntity[],
    ) {
    }
}
