import { BaseEntity } from './../../shared';

export const enum CheckItemType {
    'MAIN',
    'SUB'
}

export const enum DeleteFlag {
    'NORMAL',
    'DELETE'
}

export class CheckItem implements BaseEntity {
    constructor(
        public id?: number,
        public content?: string,
        public description?: string,
        public parentId?: number,
        public itemType?: CheckItemType,
        public delFlag?: DeleteFlag,
        public createdBy?: string,
        public createdDate?: any,
    ) {
    }
}
