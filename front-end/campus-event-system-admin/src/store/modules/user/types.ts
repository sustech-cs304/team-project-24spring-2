export type RoleType =
  | ''
  | '*'
  | 'SUPER_ADMIN'
  | 'INSTITUTE_ADMIN'
  | 'DEPARTMENT_ADMIN'
  | 'USER';

export const roleList = [
  'USER',
  'DEPARTMENT_ADMIN',
  'INSTITUTE_ADMIN',
  'SUPER_ADMIN',
];

export const roleMapInt = {
  0: 'USER',
  1: 'DEPARTMENT_ADMIN',
  2: 'INSTITUTE_ADMIN',
  3: 'SUPER_ADMIN',
};


export const rootUserID = '00000000-0000-0000-0000-000000000000';

export const basicPerm = ['DEPARTMENT_ADMIN', 'INSTITUTE_ADMIN', 'SUPER_ADMIN'];
export const auditPerm = ['INSTITUTE_ADMIN', 'SUPER_ADMIN'];
export const superPerm = ['SUPER_ADMIN'];

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
