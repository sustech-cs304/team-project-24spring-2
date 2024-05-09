export type RoleType = '' | '*' | 'SUPER_ADMIN' | 'ADMIN' | 'USER' ;
export interface UserState {
    id: string;
    nickname: string;
    real_name: string;
    description: string;
    email: string;
    phone: string;
    gender: string;
    permission_group: RoleType;
}
