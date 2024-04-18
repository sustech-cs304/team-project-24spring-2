import { DEFAULT_LAYOUT } from '../base';
import { AppRouteRecordRaw } from '../types';

const USERS: AppRouteRecordRaw = {
  path: '/users',
  name: 'users',
  component: DEFAULT_LAYOUT,
  meta: {
    locale: 'menu.users',
    requiresAuth: true,
    icon: 'icon-fire',
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
        roles: ['admin'],
      },
    },
    // {
    //   path: 'create',
    //   name: 'create',
    //   component: () => import('@/views/users/create/index.vue'),
    //   meta: {
    //     locale: 'menu.users.create',
    //     requiresAuth: true,
    //     roles: ['admin'],
    //   },
    // },
  ],
};

export default USERS;
