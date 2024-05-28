import { auditPerm } from '@/store/modules/user/types';
import { DEFAULT_LAYOUT } from '../base';
import { AppRouteRecordRaw } from '../types';

const USERS: AppRouteRecordRaw = {
  path: '/users',
  name: 'users',
  component: DEFAULT_LAYOUT,
  meta: {
    locale: 'menu.users',
    requiresAuth: true,
    icon: 'icon-user-group',
    order: 1,
  },
  children: [
    {
      path: 'manage',
      name: 'usersManage',
      component: () => import('@/views/users/manage/index.vue'),
      meta: {
        locale: 'menu.users.manage',
        requiresAuth: true,
        roles: auditPerm,
      },
    },
    // {
    //   path: 'settings',
    //   name: 'usersSettings',
    //   component: () => import('@/views/users/settings/index.vue'),
    //   meta: {
    //     locale: 'menu.users.settings',
    //     requiresAuth: true,
    //     roles: superPerm,
    //   },
    // },
  ],
};

export default USERS;
