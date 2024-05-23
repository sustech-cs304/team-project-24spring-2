export type RoleType = '' | '*' | 'SUPER_ADMIN' | 'INSTITUTE_ADMIN'| 'DEPARTMENT_ADMIN' | 'USER' ;

export interface UserState {
    id: string;
    nickname: string;
    real_name?: string;
    description?: string;
    email?: string;
    phone?: string;
    gender?: string;
    avatar_url: string;
    permission_group: RoleType;
}
