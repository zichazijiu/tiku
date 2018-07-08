import { BaseEntity } from './../../shared';

export const enum Level {
    'INFO',
    ' ERROR'
}

export const enum LogType {
    'IEMPORT',
    'DATABASE',
    'SECURITY'
}

export class LogBackup implements BaseEntity {
    constructor(
        public id?: number,
        public createdTime?: any,
        public createdBy?: string,
        public description?: string,
        public size?: number,
        public level?: Level,
        public authority?: string,
        public logType?: LogType,
    ) {
    }
}
