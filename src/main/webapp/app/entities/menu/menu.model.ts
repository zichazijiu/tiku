import { BaseEntity } from './../../shared';

export const enum DeleteFlag {
    'DELETE',
    'NORMAL'
}

export class Menu implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public url?: string,
        public label?: string,
        public parentId?: number,
        public delFlag?: DeleteFlag,
        public menuStatus?: string,
    ) {
    }
}
